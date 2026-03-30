package com.university.system.model.query;

import org.springframework.stereotype.Component;

/**
 * AIChatbot is the final-tier handler that delegates to the Python AI service.
 * Implements QueryHandler — handles any query that FAQ and KnowledgeBase cannot.
 * This class acts as a proxy/facade to the external Groq-powered AI.
 * Actual HTTP call is made by AIService; this class encapsulates the intent.
 */
@Component
public class AIChatbot implements QueryHandler {

    @Override
    public boolean canHandle(String category) {
        // AI chatbot is the fallback — it handles everything
        return true;
    }

    @Override
    public String handle(String category, String description) {
        // Marker method — actual delegation happens in QueryHandlingService
        // which injects AIService and calls this after checking FAQ/KB
        return "AI_ESCALATION_REQUIRED:" + category + ":" + description;
    }

    @Override
    public String getHandlerName() {
        return "AI_CHATBOT";
    }
}
