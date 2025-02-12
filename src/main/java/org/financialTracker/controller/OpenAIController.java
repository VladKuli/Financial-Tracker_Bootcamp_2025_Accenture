package org.financialTracker.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.financialTracker.dto.request.AdviceRequest;
import org.financialTracker.service.OpenAIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class OpenAIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAIController.class);

    @Autowired
    private OpenAIService aiService;

    @PostMapping
    public ResponseEntity<String> processExpenseAnalysis(@RequestBody AdviceRequest request, HttpServletRequest httpRequest) {
        LOGGER.info("Headers: {}", httpRequest.getHeader("Content-Type"));
        LOGGER.info("Received expense analysis request: category={}, amount={}", request.getCategory(), request.getAmount());

        String advice = aiService.generateFinancialAdvice(request.getCategory(), request.getAmount());

        LOGGER.info("Generated financial advice: {}", advice);
        return ResponseEntity.ok(advice);
    }
}
