package com.university.system.model.query;

import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * KnowledgeBaseResolver searches a structured university knowledge base.
 * Handles complex academic policy and credit transfer queries.
 * Implements QueryHandler — second tier before AI escalation.
 */
@Component
public class KnowledgeBaseResolver implements QueryHandler {

    private static final Set<String> HANDLED_CATEGORIES = Set.of(
        "ACADEMIC_POLICY", "CREDIT_TRANSFER", "CERTIFICATE", "COURSE_INQUIRY"
    );

    @Override
    public boolean canHandle(String category) {
        return HANDLED_CATEGORIES.contains(category);
    }

    @Override
    public String handle(String category, String description) {
        // Return null to allow AI service to handle queries for detailed answers
        // Knowledge Base is skipped to enable Groq AI escalation
        return null;
    }

    @Override
    public String getHandlerName() {
        return "KNOWLEDGE_BASE_RESOLVER";
    }
}
