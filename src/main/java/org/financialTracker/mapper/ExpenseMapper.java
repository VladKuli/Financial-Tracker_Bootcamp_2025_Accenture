package org.financialTracker.mapper;

import org.financialTracker.dto.ExpenseDTO;
import org.financialTracker.model.Expense;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExpenseMapper {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Convert a single Expense entity to ExpenseDTO
    public static ExpenseDTO toDTO(Expense expense) {
        if (expense == null) {
            return null;
        }
        return new ExpenseDTO(
                expense.getId(),
                expense.getAmount(),
                dateFormat.format(expense.getDate()), // Convert Date to String
                expense.getDescription(),
                (expense.getCategory() != null)
                        ? expense.getCategory().getTitle()
                        : "Uncategorized" // Extract category name
        );
    }

    // Convert a list of Expense entities to a list of ExpenseDTOs
    public static List<ExpenseDTO> toDTOList(List<Expense> expenses) {
        return expenses.stream()
                .map(ExpenseMapper::toDTO)
                .collect(Collectors.toList());
    }
}

