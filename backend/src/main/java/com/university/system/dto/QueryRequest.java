package com.university.system.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

/**
 * DTO for incoming student query submissions.
 * Decouples the API contract from internal domain models (SOLID: DIP).
 */
public class QueryRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Student name is required")
    private String studentName;

    // Optional — if not provided, IntelligentRouting will infer it
    private String category;

    @NotBlank(message = "Query description is required")
    private String description;

    private String subject;

    public QueryRequest() {}

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "QueryRequest{" +
            "studentId='" + studentId + '\'' +
            ", studentName='" + studentName + '\'' +
            ", category='" + category + '\'' +
            ", description='" + description + '\'' +
            ", subject='" + subject + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryRequest that = (QueryRequest) o;
        return Objects.equals(studentId, that.studentId) &&
            Objects.equals(studentName, that.studentName) &&
            Objects.equals(category, that.category) &&
            Objects.equals(description, that.description) &&
            Objects.equals(subject, that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, studentName, category, description, subject);
    }
}
