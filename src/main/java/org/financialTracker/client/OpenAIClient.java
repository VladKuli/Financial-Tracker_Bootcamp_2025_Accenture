package org.financialTracker.client;

import org.financialTracker.response.OpenAIResponse;
import org.financialTracker.util.FinancialPromptConstants;
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
                "model", FinancialPromptConstants.MODEL_NAME,
                "messages", new Object[]{
                        Map.of("role", "system", "content", FinancialPromptConstants.SYSTEM_ROLE),
                        Map.of("role", "user", "content", prompt)
                },
                "max_tokens", FinancialPromptConstants.MAX_TOKENS
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
