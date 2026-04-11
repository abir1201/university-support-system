"""
GroqService — encapsulates all interactions with the Groq LLM API.

OOP Design:
  - Single Responsibility: only speaks to Groq
  - Dependency Inversion: callers depend on this abstraction, not raw HTTP
  - Open/Closed: new model or prompt strategy can be added by subclassing or
    injecting a different system prompt without changing the call interface.
"""

import os
import re
from groq import Groq
from dotenv import load_dotenv
from services.diu_scraper import DIUScraper

load_dotenv()


class GroqService:
    """Facade over the Groq Python SDK."""

    def __init__(self):
        self._client = Groq(api_key=os.environ["GROQ_API_KEY"])
        self._model = os.getenv("GROQ_MODEL", "llama3-70b-8192")
        self._max_tokens = int(os.getenv("MAX_TOKENS", "1024"))
        self._temperature = float(os.getenv("TEMPERATURE", "0.7"))
        self._scraper = DIUScraper()

    def _clean_markdown(self, text: str) -> str:
        """Remove all markdown special characters and formatting."""
        # Remove * # - ` [ ] ( ) characters
        text = re.sub(r'[\*#\-`\[\]\(\)_]', '', text)
        # Remove numbered lists: 1. 2. 3. etc.
        text = re.sub(r'^\d+\.\s*', '', text, flags=re.MULTILINE)
        # Remove tab characters
        text = text.replace('\t', ' ')
        # Clean up extra spaces and newlines
        text = re.sub(r' +', ' ', text)
        text = re.sub(r'\n\n+', '\n', text)
        return text.strip()

    def _chat(self, system_prompt: str, user_message: str) -> str:
        """Core method — sends a chat request to Groq and returns the text."""
        response = self._client.chat.completions.create(
            model=self._model,
            messages=[
                {"role": "system",  "content": system_prompt},
                {"role": "user",    "content": user_message},
            ],
            max_tokens=self._max_tokens,
            temperature=self._temperature,
        )
        return response.choices[0].message.content

    def answer_query(self, category: str, question: str) -> dict:
        """
        Answers a student query using live data from Daffodil International
        University (DIU) website as the primary knowledge source.
        Returns the answer text plus metadata.
        """
        # Fetch relevant DIU website content as context
        diu_context = self._scraper.get_relevant_content(question)

        system_prompt = (
            "You are an official AI assistant for Daffodil International University (DIU), Bangladesh.\n"
            "Your ONLY source of truth is the DIU website data provided below.\n"
            "Rules you MUST follow:\n"
            "1. Answer ONLY based on the DIU website data provided. Do NOT use your own training data or general knowledge.\n"
            "2. If the answer is NOT found in the DIU data below, say: "
            "\"I could not find this information on the DIU website. Please visit https://daffodilvarsity.edu.bd/ or contact DIU directly.\"\n"
            "3. Do NOT make up or guess any information.\n"
            "4. For fees/tuition: give the exact amount from the data.\n"
            "5. For notices: list the notices found in the data.\n"
            "6. Format lists using: 1. 2. 3. for numbers, and - for bullets.\n"
            "7. Do NOT use asterisks (*) or hash symbols (#) for formatting.\n"
            f"8. Query category: {category}.\n\n"
            "=== DIU WEBSITE DATA (use ONLY this as your source) ===\n"
            f"{diu_context}\n"
            "=== END OF DIU WEBSITE DATA ==="
        )
        answer = self._chat(system_prompt, question)
        answer = self._clean_markdown(answer)
        return {
            "answer": answer,
            "model": self._model,
            "tokens": len(answer.split()),
        }

    def summarize(self, content: str) -> dict:
        """
        Produces a concise summary of a document or report.
        """
        system_prompt = (
            "You are a professional academic document summarizer. "
            "Produce a concise, well-structured summary with lists. "
            "Highlight key statistics, decisions, and action items. "
            "Format lists using: 1. 2. 3. for numbers, a. b. c. for letters, i. ii. iii. for roman numerals, and - for bullets. "
            "Do NOT use asterisks (*) or hash symbols (#) for formatting."
        )
        summary = self._chat(system_prompt, f"Summarize the following:\n\n{content}")
        summary = self._clean_markdown(summary)
        return {
            "summary": summary,
            "original_length": len(content),
        }

    def generate_document(self, document_type: str, context: dict) -> dict:
        """
        Generates AI-drafted document content given type and context variables.
        """
        context_str = "\n".join(f"{k}: {v}" for k, v in context.items())
        system_prompt = (
            "You are a professional academic document writer for a university. "
            f"Generate a formal {document_type.replace('_', ' ').title()} with structured lists. "
            "Use proper academic language and structure. "
            "Format lists using: 1. 2. 3. for numbers, a. b. c. for letters, i. ii. iii. for roman numerals, and - for bullets. "
            "Do NOT use asterisks (*) or hash symbols (#) for formatting."
        )
        user_message = (
            f"Generate a {document_type} with the following context:\n{context_str}"
        )
        content = self._chat(system_prompt, user_message)
        content = self._clean_markdown(content)
        return {
            "content": content,
            "document_type": document_type,
        }
