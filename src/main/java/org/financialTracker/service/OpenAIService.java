package org.financialTracker.service;
import org.financialTracker.client.OpenAIClient;
import org.financialTracker.repository.JpaExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

    @Autowired
    private OpenAIClient openAIClient;

    @Autowired
    private JpaExpenseRepository repository;


    public String generateFinancialAdvice(String category, double amount) {
        if (category == null || category.isEmpty() || amount <= 0) {
            return "Invalid input: Please provide a valid category and a positive expense amount.";
        }
        String prompt = String.format(
                "I have spent $%.2f on %s this month. Can you give me practical advice on how to reduce my spending in this category?",
                amount, category
        );

        try {
            return openAIClient.getResponseFromOpenAI(prompt);
        } catch (Exception e) {
            return "Error generating financial advice: " + e.getMessage();
        }
    }
}
