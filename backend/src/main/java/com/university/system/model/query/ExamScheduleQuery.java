package com.university.system.model.query;

import jakarta.persistence.*;

/**
 * Query related to exam schedules, clashes, or rescheduling.
 */
@Entity
@Table(name = "exam_schedule_queries")
public class ExamScheduleQuery extends Query {

    private String examType;      // "MIDTERM", "FINAL", "QUIZ"
    private String courseCode;
    private String requestedDate;
    private boolean hasConflict;

    public ExamScheduleQuery() {}

    public ExamScheduleQuery(String examType, String courseCode,
                              String requestedDate, boolean hasConflict) {
        this.examType = examType;
        this.courseCode = courseCode;
        this.requestedDate = requestedDate;
        this.hasConflict = hasConflict;
    }

    @Override
    public String getCategory() {
        return "EXAM_SCHEDULE";
    }

    @Override
    public int getPriority() {
        return hasConflict ? 1 : 3;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public boolean isHasConflict() {
        return hasConflict;
    }

    public void setHasConflict(boolean hasConflict) {
        this.hasConflict = hasConflict;
    }
}
