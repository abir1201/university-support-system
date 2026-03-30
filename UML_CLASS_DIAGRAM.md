# UML Class Diagram — University Management System

## Legend
```
<<abstract>>    Abstract class (MappedSuperclass)
<<interface>>   Java interface
«uses»          Dependency
«extends»       Inheritance
«implements»    Interface realization
──────          Association
──────>         Directed association
```

---

## Domain 1 — StudentQueryHandling

```
                      ┌──────────────────────────────┐
                      │  <<abstract>>                │
                      │         Query                │
                      │──────────────────────────────│
                      │ - id: Long                   │
                      │ - studentId: String          │
                      │ - studentName: String        │
                      │ - subject: String            │
                      │ - description: String        │
                      │ - status: QueryStatus        │
                      │ - aiResponse: String         │
                      │──────────────────────────────│
                      │ + getCategory(): String      │ <<abstract>>
                      │ + getPriority(): int         │ <<abstract>>
                      └──────────────────────────────┘
                                    ▲
          ┌──────────┬──────────────┼────────────────┬──────────────┐
          │          │              │                │              │
  ┌───────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────┐ ┌──────────┐
  │CourseInq..│ │ExamSchedule..│ │CreditTransfer│ │Academic  │ │Certificate│
  │──────────│ │──────────────│ │──────────────│ │PolicyQ.. │ │Query     │
  │courseCode │ │examType      │ │sourceInst.   │ │policyArea│ │certType  │
  │inquiryType│ │hasConflict   │ │creditHours   │ │reqOffResp│ │isUrgent  │
  └───────────┘ └──────────────┘ └──────────────┘ └──────────┘ └──────────┘

                      ┌──────────────────────────────┐
                      │        <<interface>>          │
                      │         QueryHandler          │
                      │──────────────────────────────│
                      │ + canHandle(cat): boolean    │
                      │ + handle(cat, desc): String  │
                      │ + getHandlerName(): String   │
                      └──────────────────────────────┘
                                    ▲
                   ┌────────────────┼────────────────┐
                   │                │                │
          ┌────────────┐  ┌──────────────────┐  ┌──────────┐
          │ FAQHandler │  │KnowledgeBaseRes..│  │AIChatbot │
          │────────────│  │──────────────────│  │──────────│
          │FAQ_MAP     │  │HANDLED_CATEGORIES│  │(fallback)│
          └────────────┘  └──────────────────┘  └──────────┘
                   │                │                │
                   └────────────────┼────────────────┘
                                    │  uses (Chain of Responsibility)
                          ┌─────────▼────────┐
                          │   QueryRouter    │
                          │──────────────────│
                          │ + route()        │
                          │ + resolveHandler │
                          └─────────┬────────┘
                                    │ uses
                    ┌───────────────┼───────────────┐
                    │               │               │
          ┌─────────────────┐  ┌────────────┐  ┌──────────────────┐
          │IntelligentRoute.│  │QueryRecord.│  │  QueryHandling   │
          │─────────────────│  │Manager     │  │  Service         │
          │ inferCategory() │  │────────────│  │──────────────────│
          └─────────────────┘  │ record()   │  │ processQuery()   │
                                │ getStats() │  │ (orchestrator)   │
                                └────────────┘  └──────────────────┘
```

---

## Domain 2 — ServiceRequestTicketManagement

```
                  ┌──────────────────────────────────┐
                  │         <<abstract>>              │
                  │         ServiceRequest            │
                  │──────────────────────────────────│
                  │ - id: Long                        │
                  │ - ticketNumber: String            │
                  │ - studentId: String               │
                  │ - status: TicketStatus            │
                  │ - priority: Priority              │
                  │──────────────────────────────────│
                  │ + getServiceType(): String        │ <<abstract>>
                  │ + getRequiredDocuments(): String[]│ <<abstract>>
                  │ + getSlaHours(): int              │ <<abstract>>
                  └──────────────────────────────────┘
                                 ▲
          ┌──────────────────────┼──────────────────────┐
          │                      │                      │
  ┌───────────────┐   ┌──────────────────────┐   ┌─────────────────────┐
  │ Certificate   │   │  AcademicCorrection  │   │  CoursePermission   │
  │ Request       │   │  Request             │   │  Request            │
  │───────────────│   │──────────────────────│   │─────────────────────│
  │ certType      │   │ correctionType       │   │ permissionType      │
  │ isUrgent      │   │ reqFacultyApproval   │   │ currentGpa          │
  │ sla: 24/72h   │   │ sla: 48/120h         │   │ sla: 48h            │
  └───────────────┘   └──────────────────────┘   └─────────────────────┘

  ┌────────────────┐  ┌──────────────────┐  ┌────────────────┐  ┌──────────────────┐
  │TicketGenerator │  │TicketCategorizer │  │StatusNotifier  │  │EscalationManager │
  │────────────────│  │──────────────────│  │────────────────│  │──────────────────│
  │ generate(type) │  │categorizePriority│  │notifyStatus()  │  │shouldEscalate()  │
  │ AtomicInt seq  │  │suggestAssignee() │  │notifyEscalate()│  │escalate()        │
  └────────────────┘  └──────────────────┘  └────────────────┘  └──────────────────┘
          │                   │                      │                   │
          └───────────────────┴──────────────────────┴───────────────────┘
                                          │ orchestrated by
                              ┌───────────▼───────────┐
                              │  TicketManagement     │
                              │  Service              │
                              │───────────────────────│
                              │ createTicket()        │
                              │ updateStatus()        │
                              │ runEscalationCheck()  │
                              └───────────────────────┘
```

---

## Domain 3 — DocumentationReporting

```
                      ┌──────────────────────────────┐
                      │        <<interface>>          │
                      │      DocumentGenerator        │
                      │──────────────────────────────│
                      │ + generate(data): String     │
                      │ + getDocumentType(): String  │
                      │ + getFileExtension(): String │
                      └──────────────────────────────┘
                                    ▲
               ┌────────────────────┼────────────────────┐
               │                    │                    │
  ┌────────────────────┐  ┌──────────────────┐  ┌──────────────────────┐
  │AcademicLetter      │  │  NoticeGenerator  │  │  MeetingReport       │
  │Generator           │  │──────────────────│  │  Generator           │
  │────────────────────│  │ NOTICE template   │  │────────────────────  │
  │ ACADEMIC_LETTER    │  │                   │  │ MEETING_REPORT tmpl  │
  │ template           │  └──────────────────┘  └──────────────────────┘
  └────────────────────┘
               │                    │                    │
               └────────────────────┼────────────────────┘
                                    │ all «use»
                           ┌────────▼───────┐
                           │TemplateFormat  │
                           │ter             │
                           │────────────────│
                           │ format()       │
                           │ addLetterhead()│
                           └────────────────┘

                      ┌──────────────────────────────┐
                      │         <<abstract>>          │
                      │         ReportManager         │
                      │──────────────────────────────│
                      │ - reportTitle: String        │
                      │ - reportContent: String      │
                      │ - aiSummary: String          │
                      │──────────────────────────────│
                      │ + compileData(): Map         │ <<abstract>>
                      │ + buildReport(data): String  │ <<abstract>>
                      │ + generate(): String         │ <<final>>
                      └──────────────────────────────┘
                                    ▲
                      ┌────────────────────────────┐
                      │     AuditReportManager     │
                      │────────────────────────────│
                      │ auditPeriod: String        │
                      │ totalTickets: int          │
                      │ resolvedTickets: int       │
                      │ escalatedTickets: int      │
                      └────────────────────────────┘

  ┌──────────────┐  ┌────────────────┐  ┌──────────────────────────────────┐
  │DataExtractor │  │ReportSummarizer│  │       DocumentationService       │
  │──────────────│  │(via AIService) │  │──────────────────────────────────│
  │extractKV()   │  │                │  │ generateDocument() [polymorphic] │
  │extractIds()  │  │                │  │ summarizeDocument()              │
  │extractStats()│  │                │  │ generateAuditReport()            │
  └──────────────┘  └────────────────┘  └──────────────────────────────────┘
```

---

## Cross-Domain Integration

```
  ┌──────────────────┐         ┌──────────────────────────────────────────┐
  │  Java AIService  │─────────│        Python FastAPI AI Service          │
  │  (WebClient)     │  HTTP   │──────────────────────────────────────────│
  │──────────────────│ ──────> │  POST /ai/query                          │
  │ query()          │         │  POST /ai/summarize                      │
  │ summarize()      │         │  POST /ai/document                       │
  │ generateDoc()    │         │──────────────────────────────────────────│
  └──────────────────┘         │  GroqService → Groq API (LLM)            │
           │                   └──────────────────────────────────────────┘
           │ used by
  ┌────────▼──────────────────────────────────────────┐
  │  QueryHandlingService  TicketManagementService    │
  │  DocumentationService                             │
  └────────────────────────────────────────────────────┘
```

---

## OOP Principles Applied

| Principle      | Where Applied |
|----------------|---------------|
| **Encapsulation**   | All fields private + Lombok accessors; FAQ_MAP and records are encapsulated in FAQHandler/QueryRecordManager |
| **Inheritance**     | Query → CourseInquiry/ExamScheduleQuery/etc; ServiceRequest → CertificateRequest/etc; ReportManager → AuditReportManager |
| **Polymorphism**    | QueryHandler.handle() called via interface — 3 different implementations; DocumentGenerator.generate() — 3 implementations; ServiceRequest.getServiceType/getSlaHours — overridden in each subclass |
| **Abstraction**     | Query and ServiceRequest are @MappedSuperclass abstract classes; ReportManager defines the generate() template |
| **SRP**             | TicketGenerator only generates numbers; TicketCategorizer only categorizes; TemplateFormatter only formats |
| **OCP**             | New query types add a class, not modify QueryRouter; new document types add a generator, not modify DocumentationService |
| **LSP**             | All Query subclasses are safely substitutable for Query; all ServiceRequest subclasses for ServiceRequest |
| **ISP**             | QueryHandler and DocumentGenerator are small, focused interfaces |
| **DIP**             | Services depend on QueryHandler interface, not FAQHandler directly; DocumentationService depends on DocumentGenerator interface |
