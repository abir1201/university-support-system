package com.university.system.model.query;

/**
 * QueryHandler interface — defines the contract for all query-handling strategies.
 * Demonstrates the INTERFACE SEGREGATION PRINCIPLE and POLYMORPHISM:
 * FAQHandler, AIChatbot, and KnowledgeBaseResolver all implement this interface
 * but differ in how they resolve queries.
 */
public interface QueryHandler {

    /**
     * Determines if this handler can process the given query category.
     */
    boolean canHandle(String category);

    /**
     * Processes the query and returns a response string.
     */
    String handle(String category, String description);

    /**
     * Returns a human-readable name for this handler type.
     */
    String getHandlerName();
}
