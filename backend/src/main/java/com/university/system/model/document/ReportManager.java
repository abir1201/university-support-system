package com.university.system.model.document;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Abstract base class for all report managers.
 * ABSTRACTION: defines the report lifecycle (compile → summarize → archive)
 * while delegating content generation to subclasses.
 *
 * Demonstrates TEMPLATE METHOD pattern and INHERITANCE root for:
 * AuditReportManager, UniversityReportManager.
 */
@MappedSuperclass
public abstract class ReportManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reportTitle;

    @Column(nullable = false)
    private String generatedBy;

    @Column(updatable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String reportContent;

    @Column(columnDefinition = "TEXT")
    private String aiSummary;

    // Template Method: subclasses define what data to compile
    public abstract Map<String, Object> compileData();

    // Template Method: subclasses define the report format
    public abstract String buildReport(Map<String, Object> data);

    // Final method — enforces the lifecycle; cannot be overridden
    public final String generate() {
        Map<String, Object> data = compileData();
        this.reportContent = buildReport(data);
        this.generatedAt = LocalDateTime.now();
        return this.reportContent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getAiSummary() {
        return aiSummary;
    }

    public void setAiSummary(String aiSummary) {
        this.aiSummary = aiSummary;
    }
}
