package com.university.system.model.document;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DataExtractor extracts structured fields from unstructured text documents.
 * Used by DocumentGeneratorAI and ReportSummarizer to pre-process raw content.
 *
 * OOP Concept: SINGLE RESPONSIBILITY — only extracts, never stores or formats.
 */
@Component
public class DataExtractor {

    /**
     * Extracts key-value pairs from a colon-separated text block.
     * Example: "Student Name: John Doe\nID: STU-001"
     */
    public Map<String, String> extractKeyValues(String text) {
        Map<String, String> result = new HashMap<>();
        Pattern pattern = Pattern.compile("^([\\w\\s]+):\\s*(.+)$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            result.put(
                matcher.group(1).trim().toUpperCase().replace(" ", "_"),
                matcher.group(2).trim()
            );
        }
        return result;
    }

    /**
     * Extracts student IDs from a document body.
     * Pattern: STU-XXXXXXXX or S followed by 8 digits.
     */
    public List<String> extractStudentIds(String text) {
        Pattern pattern = Pattern.compile("\\bSTU-\\d{8}\\b|\\bS\\d{8}\\b");
        Matcher matcher = pattern.matcher(text);
        List<String> ids = new java.util.ArrayList<>();
        while (matcher.find()) ids.add(matcher.group());
        return ids;
    }

    /**
     * Extracts numeric statistics (counts, percentages) from text.
     */
    public Map<String, Double> extractStatistics(String text) {
        Map<String, Double> stats = new HashMap<>();
        Pattern pattern = Pattern.compile("([\\w\\s]+):\\s*(\\d+\\.?\\d*)\\s*%?");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            try {
                stats.put(matcher.group(1).trim(), Double.parseDouble(matcher.group(2)));
            } catch (NumberFormatException ignored) {}
        }
        return stats;
    }
}
