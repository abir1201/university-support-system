package com.university.system.model.ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * StatusNotifier sends status update notifications to students.
 * In production, integrate with email/SMS provider.
 * Currently logs notifications — replace with JavaMailSender or Twilio.
 */
@Component
public class StatusNotifier {

    private static final Logger log = LoggerFactory.getLogger(StatusNotifier.class);

    public void notifyStatusChange(String email, String ticketNumber,
                                   ServiceRequest.TicketStatus newStatus) {
        String message = buildMessage(ticketNumber, newStatus);
        log.info("[NOTIFICATION] To: {} | Ticket: {} | Status: {} | Message: {}",
            email, ticketNumber, newStatus, message);
        // TODO: inject JavaMailSender and send actual email
    }

    public void notifyEscalation(String email, String ticketNumber) {
        log.warn("[ESCALATION NOTICE] To: {} | Ticket: {} has been escalated to dean's office.",
            email, ticketNumber);
    }

    private String buildMessage(String ticketNumber, ServiceRequest.TicketStatus status) {
        return switch (status) {
            case UNDER_REVIEW -> "Your request " + ticketNumber + " is now under review.";
            case APPROVED     -> "Your request " + ticketNumber + " has been approved.";
            case REJECTED     -> "Your request " + ticketNumber + " was rejected. Please contact the office.";
            case COMPLETED    -> "Your request " + ticketNumber + " has been completed.";
            case ESCALATED    -> "Your request " + ticketNumber + " has been escalated for priority handling.";
            default           -> "Your request " + ticketNumber + " status has been updated to: " + status;
        };
    }
}
