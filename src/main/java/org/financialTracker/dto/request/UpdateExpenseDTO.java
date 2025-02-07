package org.financialTracker.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.financialTracker.model.Category;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateExpenseDTO {
    @NotNull
    private BigDecimal amount;
    private String description;
    private Long categoryId;
}
