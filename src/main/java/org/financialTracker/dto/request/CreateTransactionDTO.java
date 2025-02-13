package org.financialTracker.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.financialTracker.model.Transaction;
import org.financialTracker.model.TransactionType;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateTransactionDTO {
    @NotNull
    private BigDecimal amount;
    private String description;
    private Long categoryId;
    @NotNull
    private TransactionType transactionType;
}
