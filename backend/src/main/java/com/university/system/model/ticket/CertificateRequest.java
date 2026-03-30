package com.university.system.model.ticket;

import jakarta.persistence.*;

/**
 * Service request for issuing academic certificates.
 * Extends ServiceRequest (INHERITANCE) and overrides abstract methods (POLYMORPHISM).
 */
@Entity
@Table(name = "certificate_requests")
public class CertificateRequest extends ServiceRequest {

    private String certificateType;       // "ENROLLMENT", "COMPLETION", "TRANSCRIPT"
    private String purpose;
    private String recipientOrganization;
    private int numberOfCopies = 1;
    private boolean requiresApostille;
    private boolean isUrgent;

    public CertificateRequest() {}

    public CertificateRequest(String certificateType, String purpose,
                               String recipientOrganization, int numberOfCopies,
                               boolean requiresApostille, boolean isUrgent) {
        this.certificateType = certificateType;
        this.purpose = purpose;
        this.recipientOrganization = recipientOrganization;
        this.numberOfCopies = numberOfCopies;
        this.requiresApostille = requiresApostille;
        this.isUrgent = isUrgent;
    }

    @Override
    public String getServiceType() {
        return "CERTIFICATE_REQUEST";
    }

    @Override
    public String[] getRequiredDocuments() {
        return new String[]{
            "Student ID Copy",
            "Request Form REG-01",
            "Fee Receipt"
        };
    }

    @Override
    public int getSlaHours() {
        return isUrgent ? 24 : 72;
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

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public boolean isRequiresApostille() {
        return requiresApostille;
    }

    public void setRequiresApostille(boolean requiresApostille) {
        this.requiresApostille = requiresApostille;
    }

    public boolean isUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }
}
