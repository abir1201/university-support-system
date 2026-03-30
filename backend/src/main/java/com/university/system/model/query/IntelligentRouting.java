package com.university.system.model.query;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * IntelligentRouting uses keyword analysis to automatically categorize
 * free-text student queries before they reach the QueryRouter.
 *
 * OOP Concept: SINGLE RESPONSIBILITY — this class only categorizes; routing
 * is QueryRouter's job.
 */
@Component
public class IntelligentRouting {

    // Keyword-to-category mapping for fast NLP-lite categorization
    private static final Map<String, String> KEYWORD_CATEGORY_MAP = Map.of(
        "exam",        "EXAM_SCHEDULE",
        "schedule",    "EXAM_SCHEDULE",
        "course",      "COURSE_INQUIRY",
        "enroll",      "COURSE_INQUIRY",
        "transfer",    "CREDIT_TRANSFER",
        "credit",      "CREDIT_TRANSFER",
        "certificate", "CERTIFICATE",
        "internship",  "INTERNSHIP_GUIDELINE",
        "deadline",    "NOTICE_DEADLINE",
        "policy",      "ACADEMIC_POLICY"
    );

    /**
     * Infers the query category from free-text description.
     * Falls back to "GENERAL" if no keywords match.
     */
    public String inferCategory(String description) {
        String lower = description.toLowerCase();
        return KEYWORD_CATEGORY_MAP.entrySet().stream()
            .filter(entry -> lower.contains(entry.getKey()))
            .map(Map.Entry::getValue)
            .findFirst()
            .orElse("GENERAL");
    }
}
