package org.financialTracker.controller;

import org.financialTracker.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class OpenAIController {

    @Autowired
    private  OpenAIService aiService;


    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeExpense(@RequestParam String category, @RequestParam double amount) {
        String advice = aiService.generateFinancialAdvice(category, amount);
        return ResponseEntity.ok(advice);
    }
}
