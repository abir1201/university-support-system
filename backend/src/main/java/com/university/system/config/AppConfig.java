package com.university.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Central application configuration.
 * Provides WebClient bean for calling the Python AI service.
 * AI_SERVICE_URL is injected from .env via system properties.
 */
@Configuration
public class AppConfig {

    @Value("${ai.service.url:http://localhost:8000}")
    private String aiServiceUrl;

    @Bean
    public WebClient aiWebClient() {
        return WebClient.builder()
            .baseUrl(aiServiceUrl)
            .defaultHeader("Content-Type", "application/json")
            .codecs(configurer ->
                configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)
            )
            .build();
    }
}
