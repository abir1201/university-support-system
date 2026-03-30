package com.university.system.model.document;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages generation of internal audit reports.
 * Extends ReportManager (INHERITANCE) and implements compileData/buildReport (POLYMORPHISM).
 */
@Entity
@Table(name = "audit_reports")
public class AuditReportManager extends ReportManager {

    private String auditPeriod;
    private String auditedDepartment;
    private int totalTickets;
    private int resolvedTickets;
    private int escalatedTickets;
    private int pendingTickets;
    private double averageResolutionHours;

    public AuditReportManager() {}

    public AuditReportManager(String auditPeriod, String auditedDepartment,
                               int totalTickets, int resolvedTickets,
                               int escalatedTickets, int pendingTickets,
                               double averageResolutionHours) {
        this.auditPeriod = auditPeriod;
        this.auditedDepartment = auditedDepartment;
        this.totalTickets = totalTickets;
        this.resolvedTickets = resolvedTickets;
        this.escalatedTickets = escalatedTickets;
        this.pendingTickets = pendingTickets;
        this.averageResolutionHours = averageResolutionHours;
    }

    @Override
    public Map<String, Object> compileData() {
        Map<String, Object> data = new HashMap<>();
        data.put("auditPeriod", auditPeriod);
        data.put("department", auditedDepartment);
        data.put("totalTickets", totalTickets);
        data.put("resolved", resolvedTickets);
        data.put("escalated", escalatedTickets);
        data.put("pending", pendingTickets);
        data.put("avgResolutionHours", averageResolutionHours);
        data.put("resolutionRate",
            totalTickets > 0 ? (resolvedTickets * 100.0 / totalTickets) : 0);
        return data;
    }

    @Override
    public String buildReport(Map<String, Object> data) {
        return """
            AUDIT REPORT — %s
            Department: %s
            ----------------------------------------
            Total Tickets     : %d
            Resolved          : %d
            Escalated         : %d
            Pending           : %d
            Resolution Rate   : %.1f%%
            Avg Resolution    : %.1f hours
            ----------------------------------------
            """.formatted(
            data.get("auditPeriod"),
            data.get("department"),
            data.get("totalTickets"),
            data.get("resolved"),
            data.get("escalated"),
            data.get("pending"),
            data.get("resolutionRate"),
            data.get("avgResolutionHours")
        );
    }

    public String getAuditPeriod() {
        return auditPeriod;
    }

    public void setAuditPeriod(String auditPeriod) {
        this.auditPeriod = auditPeriod;
    }

    public String getAuditedDepartment() {
        return auditedDepartment;
    }

    public void setAuditedDepartment(String auditedDepartment) {
        this.auditedDepartment = auditedDepartment;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getResolvedTickets() {
        return resolvedTickets;
    }

    public void setResolvedTickets(int resolvedTickets) {
        this.resolvedTickets = resolvedTickets;
    }

    public int getEscalatedTickets() {
        return escalatedTickets;
    }

    public void setEscalatedTickets(int escalatedTickets) {
        this.escalatedTickets = escalatedTickets;
    }

    public int getPendingTickets() {
        return pendingTickets;
    }

    public void setPendingTickets(int pendingTickets) {
        this.pendingTickets = pendingTickets;
    }

    public double getAverageResolutionHours() {
        return averageResolutionHours;
    }

    public void setAverageResolutionHours(double averageResolutionHours) {
        this.averageResolutionHours = averageResolutionHours;
    }
}
