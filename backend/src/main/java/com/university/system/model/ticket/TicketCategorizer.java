package com.university.system.model.ticket;

import org.springframework.stereotype.Component;

/**
 * TicketCategorizer classifies incoming requests and assigns an initial priority.
 * Uses keyword analysis on the request details.
 *
 * OOP Concept: SINGLE RESPONSIBILITY — only categorizes, never persists or routes.
 */
@Component
public class TicketCategorizer {

    public ServiceRequest.Priority categorizePriority(String details, String serviceType) {
        String lower = details.toLowerCase();

        // Urgent keywords
        if (lower.contains("urgent") || lower.contains("emergency") ||
            lower.contains("immediate") || lower.contains("asap")) {
            return ServiceRequest.Priority.URGENT;
        }

        // High-priority service types
        if ("ACADEMIC_CORRECTION".equals(serviceType) ||
            lower.contains("appeal") || lower.contains("grievance")) {
            return ServiceRequest.Priority.HIGH;
        }

        // Low priority
        if (lower.contains("whenever") || lower.contains("no rush")) {
            return ServiceRequest.Priority.LOW;
        }

        return ServiceRequest.Priority.NORMAL;
    }

    public String suggestAssignee(String serviceType) {
        return switch (serviceType) {
            case "CERTIFICATE_REQUEST"   -> "registrar-office@university.edu";
            case "ACADEMIC_CORRECTION"   -> "academic-affairs@university.edu";
            case "COURSE_PERMISSION"     -> "academic-advisor@university.edu";
            default                      -> "student-services@university.edu";
        };
    }
}
