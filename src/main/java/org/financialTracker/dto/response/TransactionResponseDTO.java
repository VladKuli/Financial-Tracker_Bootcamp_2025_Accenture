package org.financialTracker.dto.response;

import lombok.*;
import org.financialTracker.model.TransactionType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {

    private Long id;
    private BigDecimal amount;
    private String date;
    private String description;
    private String category;
    private TransactionType transactionType;

}