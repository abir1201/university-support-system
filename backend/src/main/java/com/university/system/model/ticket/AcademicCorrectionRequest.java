package com.university.system.model.ticket;

import jakarta.persistence.*;

/**
 * Request to correct academic records (grades, attendance, personal data).
 */
@Entity
@Table(name = "academic_correction_requests")
public class AcademicCorrectionRequest extends ServiceRequest {

    private String correctionType;    // "GRADE", "ATTENDANCE", "PERSONAL_DATA"
    private String courseCode;
    private String semester;
    private String currentValue;
    private String requestedValue;
    private String justification;
    private boolean requiresFacultyApproval;

    public AcademicCorrectionRequest() {}

    public AcademicCorrectionRequest(String correctionType, String courseCode,
                                      String semester, String currentValue,
                                      String requestedValue, String justification,
                                      boolean requiresFacultyApproval) {
        this.correctionType = correctionType;
        this.courseCode = courseCode;
        this.semester = semester;
        this.currentValue = currentValue;
        this.requestedValue = requestedValue;
        this.justification = justification;
        this.requiresFacultyApproval = requiresFacultyApproval;
    }

    @Override
    public String getServiceType() {
        return "ACADEMIC_CORRECTION";
    }

    @Override
    public String[] getRequiredDocuments() {
        return new String[]{
            "Supporting Evidence",
            "Faculty Endorsement Letter",
            "Academic Correction Form AC-02"
        };
    }

    @Override
    public int getSlaHours() {
        return requiresFacultyApproval ? 120 : 48;
    }

    public String getCorrectionType() {
        return correctionType;
    }

    public void setCorrectionType(String correctionType) {
        this.correctionType = correctionType;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getRequestedValue() {
        return requestedValue;
    }

    public void setRequestedValue(String requestedValue) {
        this.requestedValue = requestedValue;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public boolean isRequiresFacultyApproval() {
        return requiresFacultyApproval;
    }

    public void setRequiresFacultyApproval(boolean requiresFacultyApproval) {
        this.requiresFacultyApproval = requiresFacultyApproval;
    }
}
