package com.university.system.model.query;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * QueryRouter applies the Chain of Responsibility pattern.
 * It walks through ordered handlers (FAQ → KnowledgeBase → AI) and
 * returns the first successful response.
 *
 * OOP Concepts demonstrated:
 * - ENCAPSULATION: routing logic is hidden behind route()
 * - POLYMORPHISM: handlers are called via the QueryHandler interface
 * - OPEN/CLOSED PRINCIPLE: new handlers can be added without modifying this class
 */
@Component
public class QueryRouter {

    private final FAQHandler faqHandler;
    private final KnowledgeBaseResolver knowledgeBaseResolver;
    private final AIChatbot aiChatbot;

    public QueryRouter(FAQHandler faqHandler,
                       KnowledgeBaseResolver knowledgeBaseResolver,
                       AIChatbot aiChatbot) {
        this.faqHandler = faqHandler;
        this.knowledgeBaseResolver = knowledgeBaseResolver;
        this.aiChatbot = aiChatbot;
    }

    /**
     * Routes the query through the handler chain and returns the resolved response.
     * Returns null if the query must be handled asynchronously by the AI service.
     */
    public String route(String category, String description) {
        List<QueryHandler> chain = List.of(faqHandler, knowledgeBaseResolver, aiChatbot);

        for (QueryHandler handler : chain) {
            if (handler.canHandle(category)) {
                String response = handler.handle(category, description);
                if (response != null && !response.startsWith("AI_ESCALATION_REQUIRED")) {
                    return response;
                }
                // Signal to service layer that AI call is needed
                return null;
            }
        }
        return "Query could not be routed. Please contact support.";
    }

    /**
     * Determines which handler tier will handle this category.
     */
    public String resolveHandlerName(String category) {
        List<QueryHandler> chain = List.of(faqHandler, knowledgeBaseResolver, aiChatbot);
        for (QueryHandler handler : chain) {
            if (handler.canHandle(category)) {
                return handler.getHandlerName();
            }
        }
        return "UNKNOWN";
    }
}
