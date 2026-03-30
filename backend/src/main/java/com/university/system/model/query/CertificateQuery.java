package com.university.system.model.query;

import jakarta.persistence.*;

/**
 * Query about certificate issuance, availability, and processing time.
 */
@Entity
@Table(name = "certificate_queries")
public class CertificateQuery extends Query {

    private String certificateType;   // "ENROLLMENT", "GRADUATION", "TRANSCRIPT"
    private String purpose;
    private String recipientOrganization;
    private boolean isUrgent;

    public CertificateQuery() {}

    public CertificateQuery(String certificateType, String purpose,
                             String recipientOrganization, boolean isUrgent) {
        this.certificateType = certificateType;
        this.purpose = purpose;
        this.recipientOrganization = recipientOrganization;
        this.isUrgent = isUrgent;
    }

    @Override
    public String getCategory() {
        return "CERTIFICATE";
    }

    @Override
    public int getPriority() {
        return isUrgent ? 1 : 2;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRecipientOrganization() {
        return recipientOrganization;
    }

    public void setRecipientOrganization(String recipientOrganization) {
        this.recipientOrganization = recipientOrganization;
    }

    public boolean isUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }
}
