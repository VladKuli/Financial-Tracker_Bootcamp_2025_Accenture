package org.financialTracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.financialTracker.model.Category;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCreateDTO {
    @NotNull
    private BigDecimal amount;
    private String description;
    private Category category;
}
