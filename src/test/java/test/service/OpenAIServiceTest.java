package test.service;

import org.financialTracker.client.OpenAIClient;
import org.financialTracker.repository.JpaTransactionRepository;
import org.financialTracker.service.OpenAIService;
import org.financialTracker.util.FinancialPromptConstants;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenAIServiceTest {

    @Mock
    private OpenAIClient openAIClient;

    @Mock
    private JpaTransactionRepository repository;

    @InjectMocks
    private OpenAIService openAIService;


    @Test
    void testGenerateFinancialAdvice_ValidInput() {
        String category = "Food";
        double amount = 50.0;
        String expectedResponse = "You should consider meal prepping to save money.";

        when(openAIClient.getResponseFromOpenAI(String.format(FinancialPromptConstants.SPENDING_ADVICE_PROMPT, amount, category)))
                .thenReturn(expectedResponse);

        String actualResponse = openAIService.generateFinancialAdvice(category, amount);

        assertEquals(expectedResponse, actualResponse);
        verify(openAIClient, times(1)).getResponseFromOpenAI(anyString());
    }

    @Test
    void testGenerateFinancialAdvice_InvalidInput_NegativeAmount() {
        String response = openAIService.generateFinancialAdvice("Food", -10.0);

        assertEquals("Invalid input: Please provide a valid category and a positive transaction amount.", response);
        verify(openAIClient, never()).getResponseFromOpenAI(anyString());
    }

    @Test
    void testGenerateFinancialAdvice_InvalidInput_EmptyCategory() {
        String response = openAIService.generateFinancialAdvice("", 20.0);

        assertEquals("Invalid input: Please provide a valid category and a positive transaction amount.", response);
        verify(openAIClient, never()).getResponseFromOpenAI(anyString());
    }

}