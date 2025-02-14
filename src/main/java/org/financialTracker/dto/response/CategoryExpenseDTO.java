package org.financialTracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CategoryExpenseDTO {
    private String category;
    private BigDecimal totalAmount;
}
