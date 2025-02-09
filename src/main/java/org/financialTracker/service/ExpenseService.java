package org.financialTracker.service;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.request.UpdateExpenseDTO;
import org.financialTracker.dto.response.ExpenseResponseDTO;
import org.financialTracker.dto.request.CreateExpenseDTO;
import org.financialTracker.exception.CategoryNotFoundException;
import org.financialTracker.exception.ExpenseNotFoundException;
import org.financialTracker.mapper.ExpenseMapper;
import org.financialTracker.model.Expense;
import org.financialTracker.model.User;
import org.financialTracker.repository.JpaCategoryRepository;
import org.financialTracker.repository.JpaExpenseRepository;
import org.financialTracker.repository.JpaUserRepository;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final JpaExpenseRepository jpaExpenseRepository;
    private final AuthService authService;
    private final JpaUserRepository jpaUserRepository;
    private final JpaCategoryRepository jpaCategoryRepository;

    public List<ExpenseResponseDTO> getExpensesByUser() throws AuthException {
        User currentUser = authService.getAuthenticatedUserWoDTO();

        return ExpenseMapper.toDTOList(jpaExpenseRepository.findExpenseByUser(currentUser));
    }

    public ExpenseResponseDTO getExpensesByIdAndUser(Long id) throws AuthException {
        User currentUser = authService.getAuthenticatedUserWoDTO();

        Expense expense = jpaExpenseRepository.findExpenseByIdAndUser(id, currentUser)
                .orElseThrow(
                        () -> new ExpenseNotFoundException("Expense with id " + id + " not found")
                );

        return ExpenseMapper.toDTO(expense);
    }

    public List<ExpenseResponseDTO> getMonthlyExpenses() throws AuthException {
        User currentUser = authService.getAuthenticatedUserWoDTO();
        LocalDate startDateLocal = LocalDate.now().withDayOfMonth(1);
        LocalDate endDateLocal = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        Date startDate = Date.from(startDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return ExpenseMapper.toDTOList(jpaExpenseRepository.findExpensesForCurrentMonth(currentUser, startDate, endDate));
    }

    public ExpenseResponseDTO createExpense(CreateExpenseDTO createExpenseDTO) throws AuthException {
        User currentUser = authService.getAuthenticatedUserWoDTO();

        Expense newExpense = new Expense();
        newExpense.setAmount(createExpenseDTO.getAmount());
        newExpense.setDescription(createExpenseDTO.getDescription());
        newExpense.setCategory(
                jpaCategoryRepository.findById(createExpenseDTO.getCategoryId())
                        .orElseThrow(
                                () -> new CategoryNotFoundException("Category with id " + createExpenseDTO.getCategoryId() + " not found")
                        )
        );
        newExpense.setUser(jpaUserRepository.findByUsername(currentUser.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        ));
        jpaExpenseRepository.save(newExpense);

        return ExpenseMapper.toDTO(newExpense);
    }

    public ExpenseResponseDTO updateExpense(Long id, UpdateExpenseDTO updateExpenseDTO) throws AuthException {
        User currentUser = authService.getAuthenticatedUserWoDTO();

        Expense updatedExpense = jpaExpenseRepository.findExpenseByIdAndUser(id, currentUser)
                .orElseThrow(
                        () -> new ExpenseNotFoundException("Expense not found")
                );

        updatedExpense.setAmount(updateExpenseDTO.getAmount());
        updatedExpense.setDescription(updateExpenseDTO.getDescription());
        updatedExpense.setCategory(
                jpaCategoryRepository.findById(updateExpenseDTO.getCategoryId())
                        .orElseThrow(
                                () -> new CategoryNotFoundException("Category with id " + updateExpenseDTO.getCategoryId() + " not found")
                        )
        );
        jpaExpenseRepository.save(updatedExpense);

        return ExpenseMapper.toDTO(updatedExpense);
    }

    public void deleteExpense(Long id) throws AuthException {
        User currentUser = authService.getAuthenticatedUserWoDTO();

        Expense expense = jpaExpenseRepository.findExpenseByIdAndUser(id, currentUser)
                .orElseThrow(
                    () -> new ExpenseNotFoundException("Expense with id '" + id + "' not found")
                );

        jpaExpenseRepository.delete(expense);
    }
}
