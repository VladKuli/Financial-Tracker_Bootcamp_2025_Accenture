package org.financialTracker.mapper;

import org.financialTracker.dto.response.TransactionResponseDTO;
import org.financialTracker.model.Transaction;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Convert a single Transaction entity to TransactionDTO
    public static TransactionResponseDTO toDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAmount(),
                dateFormat.format(transaction.getDate()), // Convert Date to String
                transaction.getDescription(),
                (transaction.getCategory() != null)
                        ? transaction.getCategory().getTitle()
                        : "Uncategorized", // Extract category name
                transaction.getTransactionType()
        );
    }

    // Convert a list of Transaction entities to a list of TransactionDTOs
    public static List<TransactionResponseDTO> toDTOList(List<Transaction> transactions) {
        return transactions.stream()
                .map(TransactionMapper::toDTO)
                .collect(Collectors.toList());
    }
}

