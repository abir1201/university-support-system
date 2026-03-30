package com.university.system.model.query;

import jakarta.persistence.*;

/**
 * Represents a student inquiry about a specific course.
 * Inherits from Query (INHERITANCE) and provides concrete implementations
 * for abstract methods (POLYMORPHISM).
 */
@Entity
@Table(name = "course_inquiries")
public class CourseInquiry extends Query {

    @Column(nullable = false)
    private String courseCode;

    private String courseName;
    private String semester;
    private String inquiryType; // e.g., "ENROLLMENT", "SYLLABUS", "GRADING"

    public CourseInquiry() {}

    public CourseInquiry(String courseCode, String courseName, String semester, String inquiryType) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.semester = semester;
        this.inquiryType = inquiryType;
    }

    @Override
    public String getCategory() {
        return "COURSE_INQUIRY";
    }

    @Override
    public int getPriority() {
        return "ENROLLMENT".equals(inquiryType) ? 1 : 2;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getInquiryType() {
        return inquiryType;
    }

    public void setInquiryType(String inquiryType) {
        this.inquiryType = inquiryType;
    }
}
