"""
DIUScraper — fetches live data from Daffodil International University
official backend API and website for use as AI context.

OOP Design:
  - Single Responsibility: only fetches and structures DIU data
  - Caching: TTL-based cache (1 hour) to avoid repeated requests
  - Open/Closed: new data sources can be added via new methods

Discovered API endpoints (from DIU website JS bundles):
  Notices  : GET https://webbackend.daffodilvarsity.edu.bd/api/v1/public/notice?per_page=N
  Fees     : GET https://webbackend.daffodilvarsity.edu.bd/api/v1/public/tuition-fees
  Homepage : https://daffodilvarsity.edu.bd/ (SSR content via BeautifulSoup)
"""

import time
import httpx
from bs4 import BeautifulSoup
from typing import Optional

# DIU official backend API base
DIU_API_BASE = "https://webbackend.daffodilvarsity.edu.bd/api/v1/public"
DIU_WEBSITE   = "https://daffodilvarsity.edu.bd"

CACHE_TTL_SECONDS = 3600  # 1 hour cache


class DIUScraper:
    """Fetches and caches live data from DIU official APIs and website."""

    def __init__(self, ttl: int = CACHE_TTL_SECONDS):
        self._ttl = ttl
        self._cache: dict = {}
        self._headers = {
            "User-Agent": (
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                "AppleWebKit/537.36 (KHTML, like Gecko) "
                "Chrome/120.0.0.0 Safari/537.36"
            ),
            "Accept": "application/json, text/html, */*",
            "Referer": "https://daffodilvarsity.edu.bd/",
            "Origin": "https://daffodilvarsity.edu.bd",
        }

    # ─── Cache Helpers ────────────────────────────────────────────────────────

    def _is_fresh(self, key: str) -> bool:
        if key not in self._cache:
            return False
        return (time.time() - self._cache[key]["ts"]) < self._ttl

    def _save(self, key: str, content: str) -> str:
        self._cache[key] = {"content": content, "ts": time.time()}
        return content

    def _get_cached(self, key: str) -> Optional[str]:
        if key in self._cache:
            return self._cache[key]["content"]
        return None

    # ─── HTTP Helpers ─────────────────────────────────────────────────────────

    def _get_json(self, url: str) -> Optional[dict]:
        try:
            with httpx.Client(timeout=15, follow_redirects=True,
                              headers=self._headers) as client:
                r = client.get(url)
                r.raise_for_status()
                return r.json()
        except Exception as e:
            print(f"[DIUScraper] JSON fetch failed {url}: {e}")
            return None

    def _get_html(self, url: str) -> Optional[str]:
        try:
            with httpx.Client(timeout=15, follow_redirects=True,
                              headers=self._headers) as client:
                r = client.get(url)
                r.raise_for_status()
                return r.text
        except Exception as e:
            print(f"[DIUScraper] HTML fetch failed {url}: {e}")
            return None

    # ─── Notices ──────────────────────────────────────────────────────────────

    def get_notices(self, count: int = 20) -> str:
        """Return latest DIU notices from the official notice API."""
        key = f"notices_{count}"
        if self._is_fresh(key):
            return self._cache[key]["content"]

        data = self._get_json(
            f"{DIU_API_BASE}/notice?per_page={count}"
        )

        if not data or not data.get("status"):
            return self._get_cached(key) or (
                "Could not fetch notices. Visit: "
                "https://daffodilvarsity.edu.bd/noticeboard"
            )

        notices = data.get("data", [])
        lines = ["=== DIU NOTICEBOARD (Latest Notices) ==="]
        lines.append(f"Source: https://daffodilvarsity.edu.bd/noticeboard")
        lines.append("")

        for n in notices:
            title    = n.get("title", "")
            dept     = n.get("department", "")
            category = n.get("notice_category", "")
            date     = n.get("create_at", "")
            files    = n.get("noticeFiles", [])

            lines.append(f"- {title}")
            if dept:
                lines.append(f"  Department: {dept}")
            if category:
                lines.append(f"  Category: {category}")
            if date:
                lines.append(f"  Date: {date}")
            if files:
                for f in files:
                    lines.append(f"  File: {f.get('file_title', '')} -> {f.get('file_name', '')}")
            lines.append("")

        return self._save(key, "\n".join(lines))

    # ─── Tuition Fees ─────────────────────────────────────────────────────────

    def get_tuition_fees(self) -> str:
        """Return DIU tuition fee structure from the official fees API."""
        key = "tuition_fees"
        if self._is_fresh(key):
            return self._cache[key]["content"]

        data = self._get_json(f"{DIU_API_BASE}/tuition-fees")

        if not data or not data.get("status"):
            # Try with tuition_category_id=1 (local students)
            data = self._get_json(f"{DIU_API_BASE}/tuition-fees?tuition_category_id=1")

        if not data or not data.get("status"):
            return self._get_cached(key) or (
                "Could not fetch fees. Visit: "
                "https://daffodilvarsity.edu.bd/tuition-fees"
            )

        tuitions = data.get("tuitions", [])
        lines = ["=== DIU TUITION FEES (Local Students) ==="]
        lines.append("Source: https://daffodilvarsity.edu.bd/tuition-fees")
        lines.append("")

        # Group by faculty
        by_faculty: dict = {}
        for t in tuitions:
            fac = t.get("faculty_name", "Other")
            by_faculty.setdefault(fac, []).append(t)

        for faculty, programs in by_faculty.items():
            lines.append(f"Faculty: {faculty}")
            lines.append("-" * 40)
            for p in programs:
                prog_name = p.get("program_name", "")
                prog_type = p.get("program_type", "")
                duration  = p.get("program_duration", "")
                credits   = p.get("credit", "")
                adm_fee   = p.get("admission_fees", "")
                sem_cost  = p.get("semester_cost", "")
                total_fee = p.get("total_fees", "")
                tuition   = p.get("tuition_fees", "")
                majors    = p.get("majors", "")
                dept      = p.get("department_name", "")

                lines.append(f"  Program: {prog_name} ({prog_type})")
                lines.append(f"  Department: {dept}")
                if duration:
                    lines.append(f"  Duration: {duration}")
                if credits:
                    lines.append(f"  Total Credits: {credits}")
                if adm_fee:
                    lines.append(f"  Admission Fee: {adm_fee} BDT")
                if sem_cost:
                    lines.append(f"  Per Semester Cost: {sem_cost} BDT")
                if tuition:
                    lines.append(f"  Tuition Fee: {tuition} BDT")
                if total_fee:
                    lines.append(f"  Total Program Fee: {total_fee} BDT")
                if majors:
                    lines.append(f"  Majors: {majors}")
                lines.append("")
            lines.append("")

        return self._save(key, "\n".join(lines))

    def get_international_fees(self) -> str:
        """Return DIU tuition fee for international students (category 2)."""
        key = "int_tuition_fees"
        if self._is_fresh(key):
            return self._cache[key]["content"]

        data = self._get_json(f"{DIU_API_BASE}/tuition-fees?tuition_category_id=2")

        if not data or not data.get("status"):
            return self._get_cached(key) or (
                "Could not fetch international fees. Visit: "
                "https://daffodilvarsity.edu.bd/int-tuition-fees"
            )

        tuitions = data.get("tuitions", [])
        lines = ["=== DIU TUITION FEES (International Students) ==="]
        lines.append("Source: https://daffodilvarsity.edu.bd/int-tuition-fees")
        lines.append("")

        for t in tuitions:
            prog_name = t.get("program_name", "")
            prog_type = t.get("program_type", "")
            adm_fee   = t.get("admission_fees", "")
            total_fee = t.get("total_fees", "")
            tuition   = t.get("tuition_fees", "")

            lines.append(f"- {prog_name} ({prog_type})")
            if adm_fee:
                lines.append(f"  Admission Fee: {adm_fee} USD")
            if tuition:
                lines.append(f"  Tuition Fee: {tuition} USD")
            if total_fee:
                lines.append(f"  Total Fee: {total_fee} USD")
            lines.append("")

        return self._save(key, "\n".join(lines))

    # ─── Homepage General Info ────────────────────────────────────────────────

    def get_homepage_info(self) -> str:
        """Return general DIU info scraped from homepage (SSR content)."""
        key = "homepage"
        if self._is_fresh(key):
            return self._cache[key]["content"]

        html = self._get_html(DIU_WEBSITE)
        if not html:
            return self._get_cached(key) or (
                "Could not fetch homepage. Visit: https://daffodilvarsity.edu.bd"
            )

        soup = BeautifulSoup(html, "html.parser")
        for tag in soup(["script", "style", "nav", "footer", "header",
                         "iframe", "noscript", "form", "button"]):
            tag.decompose()

        main = (
            soup.find("main") or
            soup.find("div", id="main") or
            soup.find("body")
        )
        text = main.get_text(separator="\n", strip=True) if main else soup.get_text("\n", strip=True)
        lines = [ln for ln in text.splitlines() if len(ln.strip()) > 5]

        content = "=== DAFFODIL INTERNATIONAL UNIVERSITY (DIU) - General Info ===\n"
        content += "Source: https://daffodilvarsity.edu.bd\n\n"
        content += "\n".join(lines[:150])

        return self._save(key, content)

    # ─── Combined / Routing ───────────────────────────────────────────────────

    def get_all_content(self) -> str:
        """Return all DIU data combined."""
        parts = [
            self.get_homepage_info(),
            self.get_notices(20),
            self.get_tuition_fees(),
        ]
        return "\n\n".join(parts)

    def get_relevant_content(self, question: str) -> str:
        """
        Return only the most relevant DIU data based on question keywords.
        Reduces token usage by sending only pertinent context.
        """
        q = question.lower()

        fee_kw = [
            "fee", "fees", "tuition", "cost", "taka", "payment",
            "semester fee", "credit fee", "charge", "pay", "admission fee",
            "how much", "কত", "টাকা", "ফি", "বেতন", "খরচ",
            "total cost", "program cost", "international fee"
        ]
        notice_kw = [
            "notice", "circular", "announcement", "news", "event",
            "deadline", "schedule", "exam", "result", "date",
            "latest", "recent", "new notice", "important",
            "নোটিশ", "বিজ্ঞপ্তি", "সময়সীমা", "পরীক্ষা"
        ]
        int_fee_kw = [
            "international", "foreign", "overseas", "dollar", "usd",
            "int student", "international student"
        ]

        parts = []

        if any(kw in q for kw in int_fee_kw):
            parts.append(self.get_international_fees())
        elif any(kw in q for kw in fee_kw):
            parts.append(self.get_tuition_fees())

        if any(kw in q for kw in notice_kw):
            parts.append(self.get_notices(20))

        if not parts:
            # General question — provide homepage + notices summary
            parts.append(self.get_homepage_info())
            parts.append(self.get_notices(10))

        return "\n\n".join(parts)
