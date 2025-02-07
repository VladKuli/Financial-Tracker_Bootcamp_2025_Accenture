package org.financialTracker.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponseDTO {

    private Long id;
    private BigDecimal amount;
    private String date;
    private String description;
    private String category;

}