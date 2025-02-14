package org.financialTracker.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.financialTracker.model.Transaction;
import org.financialTracker.model.TransactionType;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CreateTransactionDTO {
    @NotNull
    private BigDecimal amount;
    private Date date;
    private String description;
    private Long categoryId;
    @NotNull
    private TransactionType transactionType;
}
