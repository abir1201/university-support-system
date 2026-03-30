package com.university.system.service;

import com.university.system.dto.TicketRequest;
import com.university.system.model.ticket.*;
import com.university.system.repository.AcademicCorrectionRequestRepository;
import com.university.system.repository.CertificateRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * TicketManagementService orchestrates the full service request lifecycle:
 *   1. Receive request DTO
 *   2. Categorize and assign priority (TicketCategorizer)
 *   3. Generate ticket number (TicketGenerator)
 *   4. Persist the appropriate subtype
 *   5. Send submission notification (StatusNotifier)
 *   6. Check for SLA breaches and escalate (EscalationManager)
 *
 * SERVICE LAYER responsibility: coordinate domain objects, handle transactions.
 */
@Service
@Transactional
public class TicketManagementService {

    private final TicketGenerator ticketGenerator;
    private final TicketCategorizer ticketCategorizer;
    private final StatusNotifier statusNotifier;
    private final EscalationManager escalationManager;
    private final CertificateRequestRepository certRepo;
    private final AcademicCorrectionRequestRepository correctionRepo;

    public TicketManagementService(TicketGenerator ticketGenerator,
                                    TicketCategorizer ticketCategorizer,
                                    StatusNotifier statusNotifier,
                                    EscalationManager escalationManager,
                                    CertificateRequestRepository certRepo,
                                    AcademicCorrectionRequestRepository correctionRepo) {
        this.ticketGenerator = ticketGenerator;
        this.ticketCategorizer = ticketCategorizer;
        this.statusNotifier = statusNotifier;
        this.escalationManager = escalationManager;
        this.certRepo = certRepo;
        this.correctionRepo = correctionRepo;
    }

    /**
     * Creates and persists a new service request ticket.
     * Uses POLYMORPHISM to handle different service types uniformly.
     */
    public ServiceRequest createTicket(TicketRequest req) {
        String ticketNumber = ticketGenerator.generate(req.getServiceType());
        ServiceRequest.Priority priority = ticketCategorizer.categorizePriority(
            req.getRequestDetails(), req.getServiceType()
        );
        String assignedTo = ticketCategorizer.suggestAssignee(req.getServiceType());

        ServiceRequest ticket = switch (req.getServiceType()) {
            case "CERTIFICATE_REQUEST" -> buildCertificateRequest(req, ticketNumber, priority, assignedTo);
            case "ACADEMIC_CORRECTION"  -> buildCorrectionRequest(req, ticketNumber, priority, assignedTo);
            case "COURSE_PERMISSION"    -> buildPermissionRequest(req, ticketNumber, priority, assignedTo);
            default -> throw new IllegalArgumentException("Unknown service type: " + req.getServiceType());
        };

        ServiceRequest saved = persistTicket(ticket);
        statusNotifier.notifyStatusChange(req.getEmail(), ticketNumber, saved.getStatus());
        return saved;
    }

    /**
     * Updates the status of a ticket by ticket number.
     */
    public ServiceRequest updateStatus(String ticketNumber,
                                       ServiceRequest.TicketStatus newStatus,
                                       String staffNotes) {
        ServiceRequest ticket = findByTicketNumber(ticketNumber);
        ticket.setStatus(newStatus);
        ticket.setStaffNotes(staffNotes);
        if (newStatus == ServiceRequest.TicketStatus.COMPLETED) {
            ticket.setResolvedAt(LocalDateTime.now());
        }
        ServiceRequest updated = persistTicket(ticket);
        statusNotifier.notifyStatusChange(ticket.getEmail(), ticketNumber, newStatus);
        return updated;
    }

    /**
     * Runs SLA check across all open tickets and escalates breaches.
     */
    @Transactional(readOnly = true)
    public Map<String, String> runEscalationCheck() {
        java.util.Map<String, String> escalations = new java.util.LinkedHashMap<>();

        certRepo.findByStatus(ServiceRequest.TicketStatus.UNDER_REVIEW).forEach(t -> {
            if (escalationManager.shouldEscalate(t)) {
                String msg = escalationManager.escalate(t);
                escalations.put(t.getTicketNumber(), msg);
                statusNotifier.notifyEscalation(t.getEmail(), t.getTicketNumber());
                certRepo.save(t);
            }
        });

        correctionRepo.findByStatus(ServiceRequest.TicketStatus.UNDER_REVIEW).forEach(t -> {
            if (escalationManager.shouldEscalate(t)) {
                String msg = escalationManager.escalate(t);
                escalations.put(t.getTicketNumber(), msg);
                statusNotifier.notifyEscalation(t.getEmail(), t.getTicketNumber());
                correctionRepo.save(t);
            }
        });

        return escalations;
    }

    // ── Private factory helpers ───────────────────────────────────────────────

    private CertificateRequest buildCertificateRequest(
        TicketRequest req, String ticketNo,
        ServiceRequest.Priority priority, String assignedTo) {

        CertificateRequest cr = new CertificateRequest();
        applyCommonFields(cr, req, ticketNo, priority, assignedTo);
        cr.setCertificateType(req.getCertificateType());
        cr.setPurpose(req.getPurpose());
        cr.setUrgent(priority == ServiceRequest.Priority.URGENT);
        return cr;
    }

    private AcademicCorrectionRequest buildCorrectionRequest(
        TicketRequest req, String ticketNo,
        ServiceRequest.Priority priority, String assignedTo) {

        AcademicCorrectionRequest acr = new AcademicCorrectionRequest();
        applyCommonFields(acr, req, ticketNo, priority, assignedTo);
        acr.setCorrectionType(req.getCorrectionType());
        acr.setCourseCode(req.getCourseCode());
        acr.setCurrentValue(req.getCurrentValue());
        acr.setRequestedValue(req.getRequestedValue());
        acr.setRequiresFacultyApproval(true);
        return acr;
    }

    private CoursePermissionRequest buildPermissionRequest(
        TicketRequest req, String ticketNo,
        ServiceRequest.Priority priority, String assignedTo) {

        CoursePermissionRequest cpr = new CoursePermissionRequest();
        applyCommonFields(cpr, req, ticketNo, priority, assignedTo);
        cpr.setCourseCode(req.getCourseCode());
        cpr.setPermissionType(req.getPermissionType());
        cpr.setReason(req.getRequestDetails());
        return cpr;
    }

    private void applyCommonFields(ServiceRequest ticket, TicketRequest req,
                                   String ticketNo, ServiceRequest.Priority priority,
                                   String assignedTo) {
        ticket.setTicketNumber(ticketNo);
        ticket.setStudentId(req.getStudentId());
        ticket.setStudentName(req.getStudentName());
        ticket.setEmail(req.getEmail());
        ticket.setRequestDetails(req.getRequestDetails());
        ticket.setPriority(priority);
        ticket.setAssignedTo(assignedTo);
        ticket.setStatus(ServiceRequest.TicketStatus.SUBMITTED);
    }

    private ServiceRequest persistTicket(ServiceRequest ticket) {
        return switch (ticket.getServiceType()) {
            case "CERTIFICATE_REQUEST" -> certRepo.save((CertificateRequest) ticket);
            case "ACADEMIC_CORRECTION" -> correctionRepo.save((AcademicCorrectionRequest) ticket);
            default -> ticket;
        };
    }

    private ServiceRequest findByTicketNumber(String ticketNumber) {
        return certRepo.findByTicketNumber(ticketNumber)
            .<ServiceRequest>map(t -> t)
            .or(() -> correctionRepo.findByTicketNumber(ticketNumber).map(t -> t))
            .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + ticketNumber));
    }
}
