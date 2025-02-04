package org.financialTracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAIConfig {

   // @Value("${openai.api.key}")
    //private String openAiApiKey;

    @Bean
    public WebClient openAIClient() {
        return WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + "something")//TODO NEED TO CHANGE TO THE KEY
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
