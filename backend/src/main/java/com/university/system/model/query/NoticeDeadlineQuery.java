package com.university.system.model.query;

import jakarta.persistence.*;

/**
 * Query about university notices, deadlines, and important dates.
 */
@Entity
@Table(name = "notice_deadline_queries")
public class NoticeDeadlineQuery extends Query {

    private String noticeType;   // "REGISTRATION", "FEE", "WITHDRAWAL", "ACADEMIC"
    private String deadline;
    private boolean reminderRequested;

    public NoticeDeadlineQuery() {}

    public NoticeDeadlineQuery(String noticeType, String deadline, boolean reminderRequested) {
        this.noticeType = noticeType;
        this.deadline = deadline;
        this.reminderRequested = reminderRequested;
    }

    @Override
    public String getCategory() {
        return "NOTICE_DEADLINE";
    }

    @Override
    public int getPriority() {
        return reminderRequested ? 2 : 3;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public boolean isReminderRequested() {
        return reminderRequested;
    }

    public void setReminderRequested(boolean reminderRequested) {
        this.reminderRequested = reminderRequested;
    }
}
