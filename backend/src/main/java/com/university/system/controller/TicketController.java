package com.university.system.controller;

import com.university.system.dto.TicketRequest;
import com.university.system.model.ticket.ServiceRequest;
import com.university.system.service.TicketManagementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for the ServiceRequestTicketManagement domain.
 */
@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:3000")
public class TicketController {

    private final TicketManagementService ticketManagementService;

    public TicketController(TicketManagementService ticketManagementService) {
        this.ticketManagementService = ticketManagementService;
    }

    /**
     * POST /api/tickets
     * Creates a new service request ticket.
     */
    @PostMapping
    public ResponseEntity<ServiceRequest> createTicket(@Valid @RequestBody TicketRequest request) {
        ServiceRequest ticket = ticketManagementService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
    }

    /**
     * PATCH /api/tickets/{ticketNumber}/status
     * Updates ticket status (staff action).
     */
    @PatchMapping("/{ticketNumber}/status")
    public ResponseEntity<ServiceRequest> updateStatus(
        @PathVariable String ticketNumber,
        @RequestParam String status,
        @RequestParam(required = false) String notes) {

        ServiceRequest.TicketStatus newStatus = ServiceRequest.TicketStatus.valueOf(status);
        ServiceRequest updated = ticketManagementService.updateStatus(ticketNumber, newStatus, notes);
        return ResponseEntity.ok(updated);
    }

    /**
     * POST /api/tickets/escalation-check
     * Runs SLA breach check and returns escalated tickets.
     */
    @PostMapping("/escalation-check")
    public ResponseEntity<Map<String, String>> runEscalationCheck() {
        Map<String, String> escalations = ticketManagementService.runEscalationCheck();
        return ResponseEntity.ok(escalations);
    }
}
