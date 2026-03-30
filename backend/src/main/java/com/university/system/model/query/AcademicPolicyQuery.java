package com.university.system.model.query;

import jakarta.persistence.*;

/**
 * Query related to academic policies — GPA, probation, graduation requirements.
 */
@Entity
@Table(name = "academic_policy_queries")
public class AcademicPolicyQuery extends Query {

    private String policyArea;   // "GPA", "PROBATION", "GRADUATION", "ATTENDANCE"
    private String policyCode;
    private boolean requiresOfficialResponse;

    public AcademicPolicyQuery() {}

    public AcademicPolicyQuery(String policyArea, String policyCode, boolean requiresOfficialResponse) {
        this.policyArea = policyArea;
        this.policyCode = policyCode;
        this.requiresOfficialResponse = requiresOfficialResponse;
    }

    @Override
    public String getCategory() {
        return "ACADEMIC_POLICY";
    }

    @Override
    public int getPriority() {
        return requiresOfficialResponse ? 1 : 3;
    }

    public String getPolicyArea() {
        return policyArea;
    }

    public void setPolicyArea(String policyArea) {
        this.policyArea = policyArea;
    }

    public String getPolicyCode() {
        return policyCode;
    }

    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    public boolean isRequiresOfficialResponse() {
        return requiresOfficialResponse;
    }

    public void setRequiresOfficialResponse(boolean requiresOfficialResponse) {
        this.requiresOfficialResponse = requiresOfficialResponse;
    }
}
