package org.financialTracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAIConfig {

    @Value("openai.api.key")
    private String openAiApiKey;
    @Value("openai.api.url")
    private String openAiApiUrl;

    @Bean
    public WebClient config() {
        return WebClient.builder()
                .baseUrl(openAiApiUrl)
                .defaultHeader("Authorization", "Bearer " + openAiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
