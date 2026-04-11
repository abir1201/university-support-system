"""
University AI Microservice — FastAPI entry point.

Architecture:
  main.py         → FastAPI app bootstrap
  routes/         → HTTP endpoint definitions (thin)
  services/       → GroqService (business logic / AI calls)
  .env            → GROQ_API_KEY and model configuration

Run:
  uvicorn main:app --reload --port 8000
"""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager
from dotenv import load_dotenv
from routes.ai_routes import router as ai_router
from services.diu_scraper import DIUScraper

load_dotenv()

# Pre-load DIU website data at startup so first query is fast
_startup_scraper = DIUScraper()


@asynccontextmanager
async def lifespan(app: FastAPI):
    """Warm up the DIU scraper cache on startup."""
    print("[Startup] Pre-loading DIU website data...")
    try:
        _startup_scraper.get_all_content()
        print("[Startup] DIU data loaded successfully.")
    except Exception as e:
        print(f"[Startup] Warning: Could not pre-load DIU data: {e}")
    yield


app = FastAPI(
    title="University AI Service",
    description="Groq-powered AI microservice for DIU student query handling, "
                "document generation, and report summarization.",
    version="2.0.0",
    lifespan=lifespan,
)

# Allow Spring Boot backend and React frontend to call this service
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8080", "http://localhost:3000"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(ai_router)


@app.get("/health")
def health_check():
    return {"status": "ok", "service": "university-ai-service"}
