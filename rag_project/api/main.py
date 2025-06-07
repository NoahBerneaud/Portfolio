from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
import requests

try:  # milvus_haystack optional for static checks
    from milvus_haystack import MilvusDocumentStore
    from milvus_haystack.milvus_embedding_retriever import MilvusEmbeddingRetriever
except ImportError:  # pragma: no cover - library optional
    MilvusDocumentStore = None  # type: ignore
    MilvusEmbeddingRetriever = None  # type: ignore

app = FastAPI(title="Simple RAG API")

MILVUS_DB_URI = "./rag_project/milvus.db"
MILVUS_COLLECTION_NAME = "piaf_1"
LM_STUDIO_ENDPOINT = "http://localhost:8080"

class Query(BaseModel):
    text: str
    top_k: int = 5

def search_in_milvus(text: str, top_k: int) -> List[str]:
    """Return ``top_k`` documents related to ``text`` using Milvus."""
    if MilvusDocumentStore is None or MilvusEmbeddingRetriever is None:
        raise HTTPException(status_code=500, detail="milvus_haystack not installed")
    try:
        doc_store = MilvusDocumentStore(
            connection_args={"uri": MILVUS_DB_URI},
            collection_name=MILVUS_COLLECTION_NAME,
        )
        retriever = MilvusEmbeddingRetriever(document_store=doc_store, top_k=top_k)
        docs = retriever.retrieve(text, top_k=top_k)
        return [d.content for d in docs]
    except Exception as exc:  # pragma: no cover - runtime depends on Haystack/Milvus
        raise HTTPException(status_code=500, detail=f"Haystack error: {exc}")

def query_lm_studio(prompt: str) -> str:
    """Send the prompt to LM Studio and return the generated answer."""
    try:
        response = requests.post(
            f"{LM_STUDIO_ENDPOINT}/v1/chat/completions",
            json={"prompt": prompt},
            timeout=30,
        )
        response.raise_for_status()
        data = response.json()
        return data.get("text", "")
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

