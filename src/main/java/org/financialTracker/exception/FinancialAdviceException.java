package org.financialTracker.exception;

import java.io.Serial;

public class FinancialAdviceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public FinancialAdviceException(String message) {
        super(message);
    }

    public FinancialAdviceException(String message, Throwable cause) {
        super(message, cause);
    }
}
