package com.university.system.model.ticket;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TicketGenerator is responsible for creating unique, traceable ticket numbers.
 * Demonstrates SINGLE RESPONSIBILITY PRINCIPLE — only generates ticket numbers.
 * Uses a thread-safe counter for uniqueness in a concurrent environment.
 *
 * Format: TKT-{YYYYMMDD}-{ServiceType}-{Sequence}
 * Example: TKT-20261130-CERT-0042
 */
@Component
public class TicketGenerator {

    private static final AtomicInteger sequence = new AtomicInteger(0);
    private static final DateTimeFormatter DATE_FORMAT =
        DateTimeFormatter.ofPattern("yyyyMMdd");

    public String generate(String serviceType) {
        String date = LocalDateTime.now().format(DATE_FORMAT);
        String abbrev = serviceType.length() >= 4
            ? serviceType.substring(0, 4).toUpperCase()
            : serviceType.toUpperCase();
        int seq = sequence.incrementAndGet();
        return String.format("TKT-%s-%s-%04d", date, abbrev, seq);
    }
}
