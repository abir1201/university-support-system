package com.university.system.model.query;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * FAQHandler handles common, repeatable queries from a static knowledge map.
 * Implements QueryHandler — demonstrates POLYMORPHISM and ENCAPSULATION.
 * This is the first line of defense before escalating to AI.
 */
@Component
public class FAQHandler implements QueryHandler {

    // Encapsulated FAQ knowledge base
    private static final Map<String, String> FAQ_MAP = new HashMap<>();
    private static final Set<String> HANDLED_CATEGORIES = Set.of(
        "NOTICE_DEADLINE", "EXAM_SCHEDULE", "INTERNSHIP_GUIDELINE"
    );

    static {
        FAQ_MAP.put("NOTICE_DEADLINE",
            "Registration deadlines are published on the academic calendar. " +
            "Visit the portal under Academic > Calendar for exact dates.");
        FAQ_MAP.put("EXAM_SCHEDULE",
            "Exam schedules are released 2 weeks before the exam period. " +
            "Check your student portal under Exams > Schedule.");
        FAQ_MAP.put("INTERNSHIP_GUIDELINE",
            "Internships require prior department approval. Submit Form INT-01 " +
            "at least 30 days before the start date.");
    }

    @Override
    public boolean canHandle(String category) {
        return HANDLED_CATEGORIES.contains(category);
    }

    @Override
    public String handle(String category, String description) {
        // Return null to enable Groq AI escalation for detailed answers
        return null;
    }

    @Override
    public String getHandlerName() {
        return "FAQ_HANDLER";
    }
}
