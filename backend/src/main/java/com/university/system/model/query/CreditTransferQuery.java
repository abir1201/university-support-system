package com.university.system.model.query;

import jakarta.persistence.*;

/**
 * Query for credit transfer requests between institutions or programs.
 */
@Entity
@Table(name = "credit_transfer_queries")
public class CreditTransferQuery extends Query {

    private String sourceInstitution;
    private String sourceCourseCode;
    private String targetCourseCode;
    private int creditHours;
    private String transferStatus; // "PENDING", "APPROVED", "REJECTED"

    public CreditTransferQuery() {}

    public CreditTransferQuery(String sourceInstitution, String sourceCourseCode,
                                String targetCourseCode, int creditHours, String transferStatus) {
        this.sourceInstitution = sourceInstitution;
        this.sourceCourseCode = sourceCourseCode;
        this.targetCourseCode = targetCourseCode;
        this.creditHours = creditHours;
        this.transferStatus = transferStatus;
    }

    @Override
    public String getCategory() {
        return "CREDIT_TRANSFER";
    }

    @Override
    public int getPriority() {
        return 2;
    }

    public String getSourceInstitution() {
        return sourceInstitution;
    }

    public void setSourceInstitution(String sourceInstitution) {
        this.sourceInstitution = sourceInstitution;
    }

    public String getSourceCourseCode() {
        return sourceCourseCode;
    }

    public void setSourceCourseCode(String sourceCourseCode) {
        this.sourceCourseCode = sourceCourseCode;
    }

    public String getTargetCourseCode() {
        return targetCourseCode;
    }

    public void setTargetCourseCode(String targetCourseCode) {
        this.targetCourseCode = targetCourseCode;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }
}
