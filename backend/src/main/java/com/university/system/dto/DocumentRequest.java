package com.university.system.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;
import java.util.Objects;

/**
 * DTO for document/report generation requests.
 */
public class DocumentRequest {

    @NotBlank(message = "Document type is required")
    private String documentType;   // "ACADEMIC_LETTER", "NOTICE", "MEETING_REPORT", "AUDIT_REPORT"

    private String generatedBy;

    // Template variables for the document
    private Map<String, String> templateData;

    // For AI-assisted summarization
    private String rawContent;

    // For audit reports
    private String auditPeriod;
    private String department;

    public DocumentRequest() {}

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public Map<String, String> getTemplateData() {
        return templateData;
    }

    public void setTemplateData(Map<String, String> templateData) {
        this.templateData = templateData;
    }

    public String getRawContent() {
        return rawContent;
    }

    public void setRawContent(String rawContent) {
        this.rawContent = rawContent;
    }

    public String getAuditPeriod() {
        return auditPeriod;
    }

    public void setAuditPeriod(String auditPeriod) {
        this.auditPeriod = auditPeriod;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "DocumentRequest{" +
            "documentType='" + documentType + '\'' +
            ", generatedBy='" + generatedBy + '\'' +
            ", auditPeriod='" + auditPeriod + '\'' +
            ", department='" + department + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentRequest that = (DocumentRequest) o;
        return Objects.equals(documentType, that.documentType) &&
            Objects.equals(generatedBy, that.generatedBy) &&
            Objects.equals(auditPeriod, that.auditPeriod) &&
            Objects.equals(department, that.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentType, generatedBy, auditPeriod, department);
    }
}
