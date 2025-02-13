package org.financialTracker.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateTransactionDTO {
    @NotNull
    private BigDecimal amount;
    private String description;
    private Long categoryId;
}
