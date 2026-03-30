package com.university.system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * AIService is the gateway to the Python FastAPI AI microservice.
 * Uses WebClient for non-blocking HTTP calls to the Groq-powered AI.
 *
 * Endpoints called:
 *   POST /ai/query     — answers student queries via Groq LLM
 *   POST /ai/summarize — summarizes long documents
 *   POST /ai/document  — generates AI-drafted document content
 *
 * OOP Concept: FACADE — hides all AI HTTP complexity behind clean methods.
 * SINGLE RESPONSIBILITY — only handles AI communication.
 */
@Service
public class AIService {

    private static final Logger log = LoggerFactory.getLogger(AIService.class);

    private final WebClient aiWebClient;

    public AIService(WebClient aiWebClient) {
        this.aiWebClient = aiWebClient;
    }

    /**
     * Sends a student query to the AI service and returns the response text.
     */
    public String query(String category, String question) {
        try {
            Map<String, String> body = Map.of(
                "category", category,
                "question", question
            );

            AIQueryResponse response = aiWebClient.post()
                .uri("/ai/query")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(AIQueryResponse.class)
                .block();

            return response != null ? response.answer() : "AI service unavailable.";

        } catch (Exception e) {
            log.error("AI query failed: {}", e.getMessage());
            return "I'm unable to process your query right now. Please contact student services.";
        }
    }

    /**
     * Summarizes a long document or report using the AI service.
     */
    public String summarize(String content) {
        try {
            Map<String, String> body = Map.of("content", content);

            AISummarizeResponse response = aiWebClient.post()
                .uri("/ai/summarize")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(AISummarizeResponse.class)
                .block();

            return response != null ? response.summary() : "Summary unavailable.";

        } catch (Exception e) {
            log.error("AI summarize failed: {}", e.getMessage());
            return "Summary generation failed.";
        }
    }

    /**
     * Requests AI-drafted document content from the AI service.
     */
    public String generateDocument(String documentType, Map<String, String> context) {
        try {
            Map<String, Object> body = Map.of(
                "document_type", documentType,
                "context", context
            );

            AIDocumentResponse response = aiWebClient.post()
                .uri("/ai/document")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(AIDocumentResponse.class)
                .block();

            return response != null ? response.content() : "Document generation unavailable.";

        } catch (Exception e) {
            log.error("AI document generation failed: {}", e.getMessage());
            return "AI document generation failed.";
        }
    }

    // ── Internal response record types ────────────────────────────────────────

    private record AIQueryResponse(String answer, String model, int tokens) {}
    private record AISummarizeResponse(String summary, int originalLength) {}
    private record AIDocumentResponse(String content, String documentType) {}
}
