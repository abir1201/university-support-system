package com.university.system.model.document;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * TemplateFormatter handles variable substitution in document templates.
 * Uses a {{placeholder}} syntax for template variables.
 *
 * OOP Concept: SINGLE RESPONSIBILITY — only formats templates.
 * All DocumentGenerator implementations depend on this class (DEPENDENCY INJECTION).
 */
@Component
public class TemplateFormatter {

    private static final DateTimeFormatter DATE_FORMAT =
        DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    /**
     * Replaces all {{key}} placeholders with corresponding values from data map.
     */
    public String format(String template, Map<String, String> data) {
        String result = template;

        // Inject standard system-level variables
        result = result.replace("{{date}}", LocalDate.now().format(DATE_FORMAT));
        result = result.replace("{{university}}", "State University");
        result = result.replace("{{registrar}}", "Office of the Registrar");

        // Inject caller-supplied data
        for (Map.Entry<String, String> entry : data.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        return result;
    }

    /**
     * Adds an official letterhead header to a document body.
     */
    public String addLetterhead(String body) {
        return """
            =====================================================
            STATE UNIVERSITY — OFFICIAL DOCUMENT
            Date: %s
            =====================================================

            %s

            =====================================================
            This document is computer-generated and officially valid.
            =====================================================
            """.formatted(LocalDate.now().format(DATE_FORMAT), body);
    }
}
