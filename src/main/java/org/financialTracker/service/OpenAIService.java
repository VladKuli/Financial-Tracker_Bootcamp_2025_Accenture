package org.financialTracker.service;
import org.financialTracker.client.OpenAIClient;
import org.financialTracker.repository.JpaExpenseRepository;
import org.financialTracker.response.OpenAIResponse;
import org.financialTracker.util.FinancialPromptConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class OpenAIService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAIService.class);

    @Autowired
    private  OpenAIClient openAIClient;
    @Autowired
    private  JpaExpenseRepository repository;

    public String generateFinancialAdvice(String category, double amount) {
        if (!isValidInput(category, amount)) {
            return new OpenAIResponse("Invalid input: Please provide a valid category and a positive expense amount.").getMessage();
        }

        String prompt = String.format(FinancialPromptConstants.SPENDING_ADVICE_PROMPT, amount, category);

        try {
            return openAIClient.getResponseFromOpenAI(prompt);
        } catch (Exception e) {
            LOGGER.error("Error generating financial advice", e);
            return new OpenAIResponse("An error occurred while generating financial advice. Please try again later.")
                    .getMessage();
        }
    }

    private boolean isValidInput(String category, double amount) {
        return category != null && !category.isBlank() && amount > 0;
    }
}
