package org.financialTracker.util;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FinancialPromptConstants {

    public static final String SPENDING_ADVICE_PROMPT =
            "I have spent $%.2f on %s this month. Can you give me practical advice on how to reduce my spending in this category? Words limit = 65";
    public static final String MODEL_NAME = "gpt-4";
    public static final String SYSTEM_ROLE = "You are a financial advisor.";
    public static final int MAX_TOKENS = 100;
}

