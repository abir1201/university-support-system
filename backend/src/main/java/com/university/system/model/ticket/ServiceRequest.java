package com.university.system.model.ticket;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Abstract base class for all service request ticket types.
 * ABSTRACTION: defines common ticket structure while leaving
 * category-specific fields to subclasses.
 *
 * INHERITANCE ROOT: CertificateRequest, AcademicCorrectionRequest,
 * and CoursePermissionRequest all extend this.
 */
@MappedSuperclass
public abstract class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ticketNumber;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.SUBMITTED;

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.NORMAL;

    @Column(columnDefinition = "TEXT")
    private String requestDetails;

    @Column(columnDefinition = "TEXT")
    private String staffNotes;

    private String assignedTo;

    @Column(updatable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    private LocalDateTime resolvedAt;

    // Template Method — subclasses define their service type
    public abstract String getServiceType();

    // Template Method — subclasses define required documents
    public abstract String[] getRequiredDocuments();

    // Template Method — SLA hours per request type
    public abstract int getSlaHours();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

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

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getRequestDetails() {
        return requestDetails;
    }

    public void setRequestDetails(String requestDetails) {
        this.requestDetails = requestDetails;
    }

    public String getStaffNotes() {
        return staffNotes;
    }

    public void setStaffNotes(String staffNotes) {
        this.staffNotes = staffNotes;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public enum TicketStatus {
        SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, ON_HOLD, COMPLETED, ESCALATED
    }

    public enum Priority {
        LOW, NORMAL, HIGH, URGENT
    }
}
