package com.university.system.model.ticket;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * EscalationManager monitors SLA breaches and escalates tickets that are overdue.
 * Implements the OPEN/CLOSED PRINCIPLE: escalation rules can be extended
 * without modifying this class by subclassing EscalationRule.
 */
@Component
public class EscalationManager {

    /**
     * Checks if a ticket has breached its SLA and should be escalated.
     */
    public boolean shouldEscalate(ServiceRequest ticket) {
        if (ticket.getSubmittedAt() == null) return false;

        long hoursElapsed = ChronoUnit.HOURS.between(
            ticket.getSubmittedAt(), LocalDateTime.now()
        );

        return hoursElapsed > ticket.getSlaHours() &&
               ticket.getStatus() != ServiceRequest.TicketStatus.COMPLETED &&
               ticket.getStatus() != ServiceRequest.TicketStatus.REJECTED;
    }

    /**
     * Escalates the ticket and returns an escalation message.
     */
    public String escalate(ServiceRequest ticket) {
        ticket.setStatus(ServiceRequest.TicketStatus.ESCALATED);
        ticket.setPriority(ServiceRequest.Priority.URGENT);
        return String.format(
            "Ticket %s escalated after SLA breach (%d hours). Assigned to dean's office.",
            ticket.getTicketNumber(), ticket.getSlaHours()
        );
    }

    /**
     * Generates an escalation report entry.
     */
    public EscalationRecord buildRecord(ServiceRequest ticket, String reason) {
        return new EscalationRecord(
            ticket.getTicketNumber(),
            ticket.getStudentId(),
            ticket.getServiceType(),
            reason,
            LocalDateTime.now()
        );
    }

    public record EscalationRecord(
        String ticketNumber,
        String studentId,
        String serviceType,
        String reason,
        LocalDateTime escalatedAt
    ) {}
}
