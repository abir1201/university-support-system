package com.university.system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

/**
 * DTO for creating a new service request ticket.
 */
public class TicketRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Service type is required")
    private String serviceType;   // "CERTIFICATE_REQUEST", "ACADEMIC_CORRECTION", "COURSE_PERMISSION"

    @NotBlank(message = "Request details are required")
    private String requestDetails;

    // Service-type-specific fields
    private String certificateType;
    private String purpose;
    private String courseCode;
    private String correctionType;
    private String currentValue;
    private String requestedValue;
    private String permissionType;

    public TicketRequest() {}

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getRequestDetails() {
        return requestDetails;
    }

    public void setRequestDetails(String requestDetails) {
        this.requestDetails = requestDetails;
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

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCorrectionType() {
        return correctionType;
    }

    public void setCorrectionType(String correctionType) {
        this.correctionType = correctionType;
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

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    @Override
    public String toString() {
        return "TicketRequest{" +
            "studentId='" + studentId + '\'' +
            ", studentName='" + studentName + '\'' +
            ", email='" + email + '\'' +
            ", serviceType='" + serviceType + '\'' +
            ", requestDetails='" + requestDetails + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketRequest that = (TicketRequest) o;
        return Objects.equals(studentId, that.studentId) &&
            Objects.equals(email, that.email) &&
            Objects.equals(serviceType, that.serviceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, email, serviceType);
    }
}
