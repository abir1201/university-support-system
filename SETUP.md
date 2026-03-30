# Setup & Run Instructions

## Project Structure

```
oop project 2/
├── backend/                    Spring Boot (Java 17)
│   ├── .env                    Environment variables (GROQ_API_KEY etc.)
│   ├── pom.xml
│   └── src/main/java/com/university/system/
│       ├── controller/         REST controllers (QueryController, TicketController, DocumentationController)
│       ├── service/            Business logic (QueryHandlingService, TicketManagementService, DocumentationService, AIService)
│       ├── repository/         Spring Data JPA repositories
│       ├── model/
│       │   ├── query/          Query hierarchy + handlers + router
│       │   ├── ticket/         ServiceRequest hierarchy + lifecycle classes
│       │   └── document/       DocumentGenerator interface + generators + ReportManager
│       ├── dto/                Request/Response DTOs
│       ├── config/             AppConfig (WebClient), SecurityConfig
│       └── exception/          GlobalExceptionHandler
│
├── ai-service/                 Python FastAPI
│   ├── .env                    GROQ_API_KEY
│   ├── main.py                 FastAPI app + CORS
│   ├── routes/ai_routes.py     /ai/query, /ai/summarize, /ai/document
│   ├── services/groq_service.py  GroqService class
│   └── requirements.txt
│
├── frontend/                   React 18
│   ├── src/
│   │   ├── App.js + App.css
│   │   ├── pages/Dashboard.jsx
│   │   ├── components/         QueryForm, TicketForm, DocumentPanel
│   │   └── services/api.js     Axios API layer
│   └── package.json
│
└── UML_CLASS_DIAGRAM.md        Full UML + OOP principles table
```

---

## 1. Configure Environment Variables

### Backend — `backend/.env`
```env
GROQ_API_KEY=gsk_your_actual_key_here
AI_SERVICE_URL=http://localhost:8000
```

### AI Service — `ai-service/.env`
```env
GROQ_API_KEY=gsk_your_actual_key_here
GROQ_MODEL=llama3-70b-8192
MAX_TOKENS=1024
TEMPERATURE=0.7
```

Get a free Groq API key at: https://console.groq.com

---

## 2. Run the Python AI Service

```bash
cd ai-service
python -m venv venv
# Windows:
venv\Scripts\activate
# macOS/Linux:
source venv/bin/activate

pip install -r requirements.txt
uvicorn main:app --reload --port 8000
```

Verify: http://localhost:8000/docs (Swagger UI)

---

## 3. Run the Spring Boot Backend

```bash
cd backend
mvn spring-boot:run
```

Verify: http://localhost:8080/h2-console (H2 database console)

---

## 4. Run the React Frontend

```bash
cd frontend
npm install
npm start
```

Opens: http://localhost:3000

---

## API Reference

### Query API
| Method | Endpoint                | Description                        |
|--------|-------------------------|------------------------------------|
| POST   | /api/queries            | Submit a student query             |
| GET    | /api/queries/records    | View routing audit log             |

**POST /api/queries — Request:**
```json
{
  "studentId": "STU-00123456",
  "studentName": "John Doe",
  "category": "COURSE_INQUIRY",
  "subject": "Course Drop Policy",
  "description": "Can I drop a course after week 8?"
}
```

### Ticket API
| Method | Endpoint                            | Description              |
|--------|-------------------------------------|--------------------------|
| POST   | /api/tickets                        | Create a ticket          |
| PATCH  | /api/tickets/{ticketNo}/status      | Update ticket status     |
| POST   | /api/tickets/escalation-check       | Run SLA breach check     |

**POST /api/tickets — Request:**
```json
{
  "studentId": "STU-00123456",
  "studentName": "Jane Smith",
  "email": "jane@university.edu",
  "serviceType": "CERTIFICATE_REQUEST",
  "requestDetails": "I need an enrollment certificate for my visa application.",
  "certificateType": "ENROLLMENT",
  "purpose": "Visa Application"
}
```

### Document API
| Method | Endpoint                    | Description                  |
|--------|-----------------------------|------------------------------|
| POST   | /api/documents/generate     | Generate a document          |
| POST   | /api/documents/summarize    | AI-summarize content         |
| POST   | /api/documents/audit-report | Generate + persist audit     |

**POST /api/documents/generate — Request:**
```json
{
  "documentType": "ACADEMIC_LETTER",
  "templateData": {
    "studentName": "John Doe",
    "studentId": "STU-00123456",
    "program": "BS Computer Science",
    "enrollmentDate": "September 2022",
    "standing": "Good Standing",
    "cgpa": "3.75",
    "purpose": "Job Application"
  }
}
```

### Python AI Endpoints
| Method | Endpoint       | Description                      |
|--------|----------------|----------------------------------|
| POST   | /ai/query      | Answer a student query via Groq  |
| POST   | /ai/summarize  | Summarize a document             |
| POST   | /ai/document   | Generate AI-drafted content      |

---

## OOP Class Hierarchy Summary

### StudentQueryHandling
```
Query (abstract)
├── CourseInquiry
├── ExamScheduleQuery
├── CreditTransferQuery
├── AcademicPolicyQuery
├── CertificateQuery
├── InternshipGuidelineQuery
└── NoticeDeadlineQuery

QueryHandler (interface)
├── FAQHandler          → tier 1: static FAQ responses
├── KnowledgeBaseResolver → tier 2: structured KB lookup
└── AIChatbot           → tier 3: Groq AI fallback

QueryRouter             → Chain of Responsibility
IntelligentRouting      → keyword-based auto-categorization
QueryRecordManager      → audit log
```

### ServiceRequestTicketManagement
```
ServiceRequest (abstract)
├── CertificateRequest
├── AcademicCorrectionRequest
└── CoursePermissionRequest

TicketGenerator         → unique ticket number factory
TicketCategorizer       → priority + assignee resolution
StatusNotifier          → email/SMS notifications
EscalationManager       → SLA breach detection + escalation
```

### DocumentationReporting
```
DocumentGenerator (interface)
├── AcademicLetterGenerator
├── NoticeGenerator
└── MeetingReportGenerator

ReportManager (abstract)
└── AuditReportManager

TemplateFormatter       → {{placeholder}} substitution
DataExtractor           → key-value and stats extraction from text
```
