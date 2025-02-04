package org.financialTracker.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {

    private Long id;
    private BigDecimal amount;
    private String date;
    private String description;
    private String category;

}