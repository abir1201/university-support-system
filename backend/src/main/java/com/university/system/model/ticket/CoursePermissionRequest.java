package com.university.system.model.ticket;

import jakarta.persistence.*;

/**
 * Request for special permission to enroll in a course (overload, prerequisite waiver).
 */
@Entity
@Table(name = "course_permission_requests")
public class CoursePermissionRequest extends ServiceRequest {

    private String courseCode;
    private String courseName;
    private String permissionType;    // "OVERLOAD", "PREREQUISITE_WAIVER", "LATE_ADD"
    private String academicAdvisor;
    private String reason;
    private double currentGpa;

    public CoursePermissionRequest() {}

    public CoursePermissionRequest(String courseCode, String courseName,
                                    String permissionType, String academicAdvisor,
                                    String reason, double currentGpa) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.permissionType = permissionType;
        this.academicAdvisor = academicAdvisor;
        this.reason = reason;
        this.currentGpa = currentGpa;
    }

    @Override
    public String getServiceType() {
        return "COURSE_PERMISSION";
    }

    @Override
    public String[] getRequiredDocuments() {
        return new String[]{
            "Academic Transcript",
            "Advisor Endorsement",
            "Permission Request Form CP-03"
        };
    }

    @Override
    public int getSlaHours() {
        return 48;
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

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    public String getAcademicAdvisor() {
        return academicAdvisor;
    }

    public void setAcademicAdvisor(String academicAdvisor) {
        this.academicAdvisor = academicAdvisor;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getCurrentGpa() {
        return currentGpa;
    }

    public void setCurrentGpa(double currentGpa) {
        this.currentGpa = currentGpa;
    }
}
