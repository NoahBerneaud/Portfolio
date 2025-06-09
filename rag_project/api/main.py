from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
import requests

from haystack import Pipeline
from haystack.utils import Secret
import wave
from milvus_haystack import MilvusDocumentStore
from haystack.components.embedders import SentenceTransformersTextEmbedder
from milvus_haystack import MilvusEmbeddingRetriever
from fastapi.staticfiles import StaticFiles
import uuid
from fastapi.responses import FileResponse
import os
from elevenlabs.client import ElevenLabs as ElevenLabsClient
import asyncio
import websockets
import json
import io



from fastapi import UploadFile, File


from fastapi.middleware.cors import CORSMiddleware

app = FastAPI(title="Simple RAG API")

# >>> CORS : must come just after instantiation <<<
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8001", "http://localhost:8080"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.mount(
    "/static",
    StaticFiles(directory="static"),
    name="static"
)

GLADIA_API_KEY = os.getenv('GLADIA_API_KEY')
ELEVEN_API_KEY = os.getenv('ELEVEN_API_KEY')
ELEVEN_VOICE_ID = os.getenv('ELEVEN_VOICE_ID', 'EXAVITQu4vr4xnSDxMaL')
MODEL_NAME = "Lajavaness/bilingual-embedding-large"
MILVUS_COLLECTION_NAME = "piaf_final_model"
LM_STUDIO_ENDPOINT = "http://localhost:1234"

# Instantiate ElevenLabs client
if not ELEVEN_API_KEY:
    raise RuntimeError("ELEVEN_API_KEY must be set in environment variables")
eleven_client = ElevenLabsClient(api_key=ELEVEN_API_KEY)

class Query(BaseModel):
    text: str
    top_k: int = 5

def search_in_milvus(text: str, top_k: int) -> List[str]:
    """Return ``top_k`` documents related to ``text`` using Haystack + Milvus."""
    # Vérification rapide
    if MilvusDocumentStore is None or MilvusEmbeddingRetriever is None:
        raise HTTPException(
            status_code=500,
            detail="haystack-ai et milvus-haystack doivent être installés"
        )

    try:
        # 1) Initialisation du DocumentStore
        document_store = MilvusDocumentStore(
            connection_args={"uri": "../milvus.db"},
            drop_old=False,
            collection_name=MILVUS_COLLECTION_NAME
        )

        # 2) Création du pipeline
        retrieval_pipeline = Pipeline()
        # (a) Embedder — ici l’embedder official OpenAI
        embedder = SentenceTransformersTextEmbedder(model=MODEL_NAME,trust_remote_code=True)
        retrieval_pipeline.add_component("embedder", embedder)
        # (b) Retriever
        retriever = MilvusEmbeddingRetriever(
            document_store=document_store,
            top_k=top_k
        )
        retrieval_pipeline.add_component("retriever", retriever)
        retrieval_pipeline.connect("embedder", "retriever")

        # 3) Exécution de la recherche
        # on met le texte en entrée de l’embedder
        pipeline_result = retrieval_pipeline.run(
            {"embedder": {"text": text}}
        )
        # 4) Extraction et renvoi des contenus
        docs = pipeline_result["retriever"]["documents"]
        return [doc.content for doc in docs]

    except Exception as exc:
        # Pour que vos tests restent verts, on capture tout
        raise HTTPException(
            status_code=500,
            detail=f"Erreur Haystack/Milvus : {exc}"
        )

def query_lm_studio(prompt: str) -> str:
    """Send the prompt to LM Studio and return the generated answer."""
    try:
        print(f"Querying LM Studio with prompt: {prompt}")  # Debugging line
        response = requests.post(
            f"{LM_STUDIO_ENDPOINT}/v1/chat/completions",
            json={"messages": [{"role": "user", "content": prompt}]},
        )
        response.raise_for_status()
        data = response.json()
        return data.get("choices", [{}])[0].get("message", {}).get("content", "")
    except Exception as exc:  # pragma: no cover - network call
        raise HTTPException(status_code=500, detail=f"LM Studio error: {exc}")

@app.post("/rag")
def rag(query: Query):
    """Perform a retrieval augmented generation."""
    docs = search_in_milvus(query.text, query.top_k)
    context = "\n".join(docs)
    prompt = f"{context}\n\nQuestion: {query.text}\nAnswer:"
    answer = query_lm_studio(prompt)
    return {"answer": answer}


@app.get("/", response_class=FileResponse)
async def serve_ui():
    # renvoie directement static/index.html
    return "static/index.html"


# --- Audio endpoint: Gladia live STT -> RAG -> ElevenLabs TTS ---
@app.post("/rag/audio")
async def rag_audio(audio: UploadFile = File(...)):
    # 1) Read raw audio and save for debugging
    raw_bytes = await audio.read()
    print(f"[rag_audio] Received file: {audio.filename}, {len(raw_bytes)} bytes")
    debug_in = f"input_{uuid.uuid4().hex}.wav"
    path_in = os.path.join("static", debug_in)
    with open(path_in, "wb") as f_in:
        f_in.write(raw_bytes)
    print(f"[rag_audio] Saved incoming WAV to {path_in}")

    # 2) Convert incoming audio to PCM 16kHz mono 16-bit
    try:
        from pydub import AudioSegment
    except ImportError:
        raise HTTPException(status_code=500, detail="pydub is required for audio format conversion")
    audio_segment = AudioSegment.from_file(io.BytesIO(raw_bytes))
    audio_segment = audio_segment.set_frame_rate(16000).set_channels(1).set_sample_width(2)
    frames = audio_segment.raw_data
    print(f"[rag_audio] Converted audio: {len(frames)} bytes of PCM frames")

    # 3) Initiate live session with Gladia with Gladia
    init_payload = {
        "encoding": "wav/pcm",
        "bit_depth": 16,
        "sample_rate": 16000,
        "channels": 1,
        "model": "solaria-1",
        "messages_config": {"receive_final_transcripts": True}
    }
    headers = {"x-gladia-key": GLADIA_API_KEY, "Content-Type": "application/json"}
    print("[rag_audio] Initiating Gladia session...")
    init_resp = requests.post("https://api.gladia.io/v2/live", json=init_payload, headers=headers)
    init_resp.raise_for_status()
    init_data = init_resp.json()
    ws_url, session_id = init_data["url"], init_data["id"]
    print(f"[rag_audio] Session {session_id}, ws_url={ws_url}")

    # 4) Stream PCM over WebSocket
    transcript = ""
    async with websockets.connect(ws_url) as ws:
        chunk_size = 4096
        total = len(frames)
        for i in range(0, total, chunk_size):
            await ws.send(frames[i:i+chunk_size])
        await ws.send(json.dumps({"type": "stop_recording"}))
        print("[rag_audio] Sent stop signal, awaiting transcript...")
        async for msg in ws:
            data = json.loads(msg)
            if data.get("type") == "transcript" and data.get("data", {}).get("is_final"):
                transcript = data["data"]["utterance"]["text"]
                print(f"[rag_audio] Transcript: {transcript}")
                break

    # 5) RAG
    docs = search_in_milvus(transcript, top_k=5)
    context = "\n".join(docs)
    prompt = f"{context}\n\nQuestion: {transcript}\nAnswer:"
    answer = query_lm_studio(prompt)
    print(f"[rag_audio] RAG answer: {answer}")

   # 6) TTS via ElevenLabs SDK, collect generator chunks
    print("[rag_audio] Generating TTS audio via ElevenLabs...")
    stream = eleven_client.text_to_speech.convert(
        text=answer,
        voice_id=ELEVEN_VOICE_ID,
        model_id="eleven_multilingual_v2",
        output_format="mp3_44100_128"
    )
    audio_bytes = b""
    try:
        for chunk in stream:
            audio_bytes += chunk
    except TypeError:
        # non-iterable, assume raw bytes
        audio_bytes = stream
    print(f"[rag_audio] Received TTS audio: {len(audio_bytes)} bytes")

    # 7) Save output and respond
    out_name = f"response_{uuid.uuid4().hex}.mp3"
    out_path = os.path.join("static", out_name)
    with open(out_path, "wb") as f:
        f.write(audio_bytes)
    print(f"[rag_audio] Saved TTS to {out_path}")
    return {"answer": answer, "audio_url": f"/static/{out_name}"}
