package org.financialTracker.service;

import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.ExpenseDTO;
import org.financialTracker.exception.ExpenseNotFoundException;
import org.financialTracker.mapper.ExpenseMapper;
import org.financialTracker.model.Expense;
import org.financialTracker.repository.JpaExpenseRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final JpaExpenseRepository jpaExpenseRepository;

    public List<ExpenseDTO> getExpensesByFilter(BigDecimal amount, LocalDate date, String categoryTitle) {
        List<Expense> expenses = jpaExpenseRepository.findExpensesByFilter(amount, date, categoryTitle);
        return ExpenseMapper.toDTOList(expenses);
    }

    public ExpenseDTO getExpenseById(Long id) {
        Expense expense = jpaExpenseRepository.findById(id).orElseThrow(
                () -> new ExpenseNotFoundException("Expense with id: '" + id + "' not found")
        );
        return ExpenseMapper.toDTO(expense);
    }

    public ExpenseDTO createExpense(Expense expense) {
        jpaExpenseRepository.save(expense);
        return ExpenseMapper.toDTO(expense);
    }

    public ExpenseDTO updateExpense(Long id, Expense expense) {
        Expense updatedExpense = jpaExpenseRepository.findById(id).orElseThrow(
                () -> new ExpenseNotFoundException("Expense with id '" + id + "' not found")
        );

        updatedExpense.setAmount(expense.getAmount());
        updatedExpense.setDescription(expense.getDescription());
        updatedExpense.setCategory(expense.getCategory());
        jpaExpenseRepository.save(updatedExpense);

        return ExpenseMapper.toDTO(updatedExpense);
    }

    public void deleteExpense(Long id) {
        if (!jpaExpenseRepository.existsById(id)) {
            throw new ExpenseNotFoundException("Expense with id '" + id + "' not found");
        }
        jpaExpenseRepository.deleteById(id);
    }
}
