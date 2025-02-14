package test.service;
import org.financialTracker.client.OpenAIClient;
import org.financialTracker.exception.FinancialAdviceException;
import org.financialTracker.response.OpenAIResponse;
import org.financialTracker.service.OpenAIService;
import org.financialTracker.util.FinancialPromptConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

class OpenAIServiceTest {

    @InjectMocks
    private OpenAIService openAIService;

    @Mock
    private OpenAIClient openAIClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateFinancialAdvice_InvalidInput() {
        assertThrows(FinancialAdviceException.class, () -> openAIService.generateFinancialAdvice("", -100));
        assertThrows(FinancialAdviceException.class, () -> openAIService.generateFinancialAdvice("Food", -100));
        assertThrows(FinancialAdviceException.class, () -> openAIService.generateFinancialAdvice("", 100));
    }

    @Test
    void testGenerateFinancialAdvice_SuccessfulResponse() throws Exception {
        String expectedResponse = "Financial advice based on your spending.";
        Mockito.when(openAIClient.getResponseFromOpenAI(anyString())).thenReturn(expectedResponse);

        String result = openAIService.generateFinancialAdvice("Food", 100);

        assertEquals(expectedResponse, result);
    }

}
