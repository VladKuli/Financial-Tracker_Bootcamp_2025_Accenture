package test.service;

import org.financialTracker.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.ZoneId;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.financialTracker.dto.ExpenseDTO;
import org.financialTracker.exception.ExpenseNotFoundException;
import org.financialTracker.mapper.ExpenseMapper;
import org.financialTracker.model.Expense;
import org.financialTracker.repository.JpaExpenseRepository;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private JpaExpenseRepository jpaExpenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense expense;
    private ExpenseDTO expenseDTO;

    @BeforeEach
    void setUp() {
        expense = new Expense();
        expense.setId(1L);
        expense.setAmount(BigDecimal.valueOf(50.00));
        expense.setDescription("Dinner");
        expense.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        expenseDTO = ExpenseMapper.toDTO(expense);
    }

    @Test
    void testGetExpensesByFilter() {
        when(jpaExpenseRepository.findExpensesByFilter(any(), any(), any())).thenReturn(List.of(expense));
        List<ExpenseDTO> result = expenseService.getExpensesByFilter(BigDecimal.valueOf(50.00), LocalDate.now(), "Food");
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jpaExpenseRepository, times(1)).findExpensesByFilter(any(), any(), any());
    }

    @Test
    void testGetExpenseById_Found() {
        when(jpaExpenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        ExpenseDTO result = expenseService.getExpenseById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(jpaExpenseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetExpenseById_NotFound() {
        when(jpaExpenseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ExpenseNotFoundException.class, () -> expenseService.getExpenseById(1L));
        verify(jpaExpenseRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateExpense() {
        when(jpaExpenseRepository.save(expense)).thenReturn(expense);
        ExpenseDTO result = expenseService.createExpense(expense);
        assertNotNull(result);
        assertEquals("Dinner", result.getDescription());
        verify(jpaExpenseRepository, times(1)).save(expense);
    }

    @Test
    void testUpdateExpense_Found() {
        Expense updatedExpense = new Expense();
        updatedExpense.setAmount(BigDecimal.valueOf(100.00));
        updatedExpense.setDescription("Updated Dinner");
        updatedExpense.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        when(jpaExpenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        when(jpaExpenseRepository.save(any(Expense.class))).thenReturn(updatedExpense);

        ExpenseDTO result = expenseService.updateExpense(1L, updatedExpense);
        assertNotNull(result);
        assertEquals("Updated Dinner", result.getDescription());
        assertEquals(BigDecimal.valueOf(100.00), result.getAmount());
        verify(jpaExpenseRepository, times(1)).findById(1L);
        verify(jpaExpenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void testUpdateExpense_NotFound() {
        when(jpaExpenseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ExpenseNotFoundException.class, () -> expenseService.updateExpense(1L, expense));
        verify(jpaExpenseRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteExpense_Found() {
        when(jpaExpenseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(jpaExpenseRepository).deleteById(1L);

        assertDoesNotThrow(() -> expenseService.deleteExpense(1L));
        verify(jpaExpenseRepository, times(1)).existsById(1L);
        verify(jpaExpenseRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteExpense_NotFound() {
        when(jpaExpenseRepository.existsById(1L)).thenReturn(false);
        assertThrows(ExpenseNotFoundException.class, () -> expenseService.deleteExpense(1L));
        verify(jpaExpenseRepository, times(1)).existsById(1L);
    }
}
