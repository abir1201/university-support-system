"""
AI Routes — FastAPI router for all AI endpoints.

Endpoints:
  POST /ai/query      — answer a student query using Groq
  POST /ai/summarize  — summarize a document
  POST /ai/document   — generate AI-drafted document content

OOP Design:
  - Routes are thin — all logic delegated to GroqService
  - Pydantic models enforce input contracts (ENCAPSULATION)
"""

from fastapi import APIRouter, HTTPException
from pydantic import BaseModel, Field
from typing import Optional
from services.groq_service import GroqService

router = APIRouter(prefix="/ai", tags=["AI"])

# Singleton service instance (in production use DI framework)
_groq = GroqService()


# ── Request/Response Schemas ──────────────────────────────────────────────────

class QueryRequest(BaseModel):
    category: str = Field(..., description="Query category, e.g. COURSE_INQUIRY")
    question: str = Field(..., min_length=5, description="The student's question")


class QueryResponse(BaseModel):
    answer: str
    model: str
    tokens: int


class SummarizeRequest(BaseModel):
    content: str = Field(..., min_length=20, description="Document text to summarize")


class SummarizeResponse(BaseModel):
    summary: str
    original_length: int


class DocumentRequest(BaseModel):
    document_type: str = Field(..., description="e.g. ACADEMIC_LETTER, NOTICE")
    context: dict = Field(default_factory=dict)


class DocumentResponse(BaseModel):
    content: str
    document_type: str


# ── Endpoints ─────────────────────────────────────────────────────────────────

@router.post("/query", response_model=QueryResponse)
async def query_endpoint(request: QueryRequest):
    """
    Answers a student query using Groq LLM.
    Called by Java's AIService when FAQ and KnowledgeBase cannot handle the query.
    """
    try:
        result = _groq.answer_query(request.category, request.question)
        return QueryResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=503, detail=f"AI service error: {str(e)}")


@router.post("/summarize", response_model=SummarizeResponse)
async def summarize_endpoint(request: SummarizeRequest):
    """
    Summarizes a document or report.
    Used by DocumentationService for audit report AI summaries.
    """
    try:
        result = _groq.summarize(request.content)
        return SummarizeResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=503, detail=f"AI service error: {str(e)}")


@router.post("/document", response_model=DocumentResponse)
async def document_endpoint(request: DocumentRequest):
    """
    Generates AI-drafted document content.
    Used when staff requests AI-enriched document generation.
    """
    try:
        result = _groq.generate_document(request.document_type, request.context)
        return DocumentResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=503, detail=f"AI service error: {str(e)}")
