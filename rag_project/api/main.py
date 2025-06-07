from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
import requests

from haystack import Pipeline
from haystack.utils import Secret
from milvus_haystack import MilvusDocumentStore
from haystack.components.embedders import SentenceTransformersTextEmbedder
from milvus_haystack import MilvusEmbeddingRetriever

app = FastAPI(title="Simple RAG API")

MODEL_NAME = "Lajavaness/bilingual-embedding-large"

MILVUS_COLLECTION_NAME = "piaf_final_model"
LM_STUDIO_ENDPOINT = "http://localhost:1234"

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

@app.get("/")
def read_root():
    return {"message": "Simple RAG API"}

