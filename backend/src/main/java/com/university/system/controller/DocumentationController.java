package com.university.system.controller;

import com.university.system.dto.DocumentRequest;
import com.university.system.model.document.AuditReportManager;
import com.university.system.service.DocumentationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for the DocumentationReporting domain.
 */
@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:3000")
public class DocumentationController {

    private final DocumentationService documentationService;

    public DocumentationController(DocumentationService documentationService) {
        this.documentationService = documentationService;
    }

    /**
     * POST /api/documents/generate
     * Generates a document by type using template data.
     */
    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateDocument(
        @Valid @RequestBody DocumentRequest request) {

        String content = documentationService.generateDocument(request);
        return ResponseEntity.ok(Map.of(
            "documentType", request.getDocumentType(),
            "content", content
        ));
    }

    /**
     * POST /api/documents/summarize
     * Summarizes raw document content using the AI service.
     */
    @PostMapping("/summarize")
    public ResponseEntity<Map<String, String>> summarize(
        @RequestBody Map<String, String> body) {

        String summary = documentationService.summarizeDocument(body.get("content"));
        return ResponseEntity.ok(Map.of("summary", summary));
    }

    /**
     * POST /api/documents/audit-report
     * Generates and persists an audit report with AI summary.
     */
    @PostMapping("/audit-report")
    public ResponseEntity<AuditReportManager> generateAuditReport(
        @RequestBody DocumentRequest request) {

        AuditReportManager report = documentationService.generateAuditReport(request);
        return ResponseEntity.ok(report);
    }
}
