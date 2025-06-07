from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
import requests

try:  # haystack-ai optional for static checks
    from haystack.document_stores import MilvusDocumentStore
    from haystack.nodes import EmbeddingRetriever
except ImportError:  # pragma: no cover - library optional
    MilvusDocumentStore = None  # type: ignore
    EmbeddingRetriever = None  # type: ignore

app = FastAPI(title="Simple RAG API")

MILVUS_COLLECTION_NAME = "documents"
LM_STUDIO_ENDPOINT = "http://localhost:8080"

class Query(BaseModel):
    text: str
    top_k: int = 5

def search_in_milvus(text: str, top_k: int) -> List[str]:
    """Return ``top_k`` documents related to ``text`` using Haystack."""
    if MilvusDocumentStore is None or EmbeddingRetriever is None:
        raise HTTPException(status_code=500, detail="haystack-ai not installed")
    try:
        doc_store = MilvusDocumentStore(collection_name=MILVUS_COLLECTION_NAME)
        retriever = EmbeddingRetriever(document_store=doc_store)
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

