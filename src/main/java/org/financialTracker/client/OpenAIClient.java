package org.financialTracker.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@Component
public class OpenAIClient {

    @Autowired
    private WebClient openAIClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getResponseFromOpenAI(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4",
                "messages", new Object[]{
                        Map.of("role", "system", "content", "You are a financial advisor."),
                        Map.of("role", "user", "content", prompt)
                },
                "max_tokens", 100
        );

        try {
            String response = openAIClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode jsonResponse = objectMapper.readTree(response);
            return jsonResponse.get("choices").get(0).get("message").get("content").asText();

        } catch (Exception e) {
            return "Error getting financial advice: " + e.getMessage();
        }
    }
}
