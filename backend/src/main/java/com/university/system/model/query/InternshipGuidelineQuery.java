package com.university.system.model.query;

import jakarta.persistence.*;

/**
 * Query about internship programs, requirements, and company approvals.
 */
@Entity
@Table(name = "internship_guideline_queries")
public class InternshipGuidelineQuery extends Query {

    private String companyName;
    private String internshipType;   // "LOCAL", "INTERNATIONAL", "VIRTUAL"
    private String program;
    private String expectedStartDate;

    public InternshipGuidelineQuery() {}

    public InternshipGuidelineQuery(String companyName, String internshipType,
                                     String program, String expectedStartDate) {
        this.companyName = companyName;
        this.internshipType = internshipType;
        this.program = program;
        this.expectedStartDate = expectedStartDate;
    }

    @Override
    public String getCategory() {
        return "INTERNSHIP_GUIDELINE";
    }

    @Override
    public int getPriority() {
        return 2;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInternshipType() {
        return internshipType;
    }

    public void setInternshipType(String internshipType) {
        this.internshipType = internshipType;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getExpectedStartDate() {
        return expectedStartDate;
    }

    public void setExpectedStartDate(String expectedStartDate) {
        this.expectedStartDate = expectedStartDate;
    }
}
