package com.university.system.service;

import com.university.system.dto.DocumentRequest;
import com.university.system.model.document.*;
import com.university.system.repository.AuditReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * DocumentationService orchestrates document and report generation:
 *   1. Selects the correct DocumentGenerator by type (POLYMORPHISM via interface)
 *   2. Delegates to AI for enrichment (DocumentGeneratorAI, ReportSummarizer)
 *   3. Persists reports to the repository
 *   4. Returns the final document content to the controller
 *
 * Demonstrates OPEN/CLOSED PRINCIPLE: adding a new document type only requires
 * adding a new DocumentGenerator implementation — this service never changes.
 */
@Service
@Transactional
public class DocumentationService {

    private final AcademicLetterGenerator academicLetterGenerator;
    private final NoticeGenerator noticeGenerator;
    private final MeetingReportGenerator meetingReportGenerator;
    private final TemplateFormatter templateFormatter;
    private final DataExtractor dataExtractor;
    private final AIService aiService;
    private final AuditReportRepository auditReportRepository;

    public DocumentationService(AcademicLetterGenerator academicLetterGenerator,
                                 NoticeGenerator noticeGenerator,
                                 MeetingReportGenerator meetingReportGenerator,
                                 TemplateFormatter templateFormatter,
                                 DataExtractor dataExtractor,
                                 AIService aiService,
                                 AuditReportRepository auditReportRepository) {
        this.academicLetterGenerator = academicLetterGenerator;
        this.noticeGenerator = noticeGenerator;
        this.meetingReportGenerator = meetingReportGenerator;
        this.templateFormatter = templateFormatter;
        this.dataExtractor = dataExtractor;
        this.aiService = aiService;
        this.auditReportRepository = auditReportRepository;
    }

    /**
     * Generates a document of the requested type.
     * POLYMORPHISM: all generators share the DocumentGenerator interface.
     */
    public String generateDocument(DocumentRequest request) {
        DocumentGenerator generator = resolveGenerator(request.getDocumentType());
        Map<String, String> data = request.getTemplateData() != null
            ? request.getTemplateData()
            : Map.of();

        String content = generator.generate(data);

        // Optionally enrich with AI-drafted content
        if (Boolean.TRUE.equals(request.getTemplateData() != null &&
                request.getTemplateData().containsKey("aiEnrich"))) {
            String aiDraft = aiService.generateDocument(request.getDocumentType(), data);
            content = content + "\n\n--- AI Suggested Content ---\n" + aiDraft;
        }

        return content;
    }

    /**
     * Summarizes a document using the AI service.
     */
    public String summarizeDocument(String rawContent) {
        return aiService.summarize(rawContent);
    }

    /**
     * Generates an audit report and persists it.
     */
    public AuditReportManager generateAuditReport(DocumentRequest request) {
        AuditReportManager report = new AuditReportManager();
        report.setReportTitle("Audit Report — " + request.getAuditPeriod());
        report.setGeneratedBy(request.getGeneratedBy() != null ? request.getGeneratedBy() : "System");
        report.setAuditPeriod(request.getAuditPeriod());
        report.setAuditedDepartment(request.getDepartment());

        // Compile statistics from extracted data
        if (request.getRawContent() != null) {
            Map<String, Double> stats = dataExtractor.extractStatistics(request.getRawContent());
            report.setTotalTickets(stats.getOrDefault("TOTAL_TICKETS", 0.0).intValue());
            report.setResolvedTickets(stats.getOrDefault("RESOLVED", 0.0).intValue());
            report.setEscalatedTickets(stats.getOrDefault("ESCALATED", 0.0).intValue());
        }

        report.generate();

        // AI summary
        String summary = aiService.summarize(report.getReportContent());
        report.setAiSummary(summary);

        return auditReportRepository.save(report);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private DocumentGenerator resolveGenerator(String documentType) {
        return switch (documentType) {
            case "ACADEMIC_LETTER" -> academicLetterGenerator;
            case "NOTICE"          -> noticeGenerator;
            case "MEETING_REPORT"  -> meetingReportGenerator;
            default -> throw new IllegalArgumentException(
                "Unknown document type: " + documentType);
        };
    }
}
