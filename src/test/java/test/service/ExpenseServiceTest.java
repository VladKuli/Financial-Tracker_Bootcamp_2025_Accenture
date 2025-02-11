package test.service;
/*
import org.financialTracker.dto.request.CreateExpenseDTO;
import org.financialTracker.dto.response.ExpenseResponseDTO;
import org.financialTracker.exception.ExpenseNotFoundException;
import org.financialTracker.mapper.ExpenseMapper;
import org.financialTracker.model.Expense;
import org.financialTracker.repository.JpaExpenseRepository;
import org.financialTracker.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private JpaExpenseRepository jpaExpenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense expense;
    private ExpenseResponseDTO expenseDTO;

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
    void testGetExpensesByUser() {
        when(jpaExpenseRepository.getExpensesByUser()).thenReturn(List.of(expense));
        List<ExpenseResponseDTO> result = expenseService.getExpensesByUser();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jpaExpenseRepository, times(1)).getExpensesByUser();
    }

    @Test
    void testGetExpenseById_Found() {
        when(jpaExpenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        ExpenseResponseDTO result = expenseService.getExpenseById(1L);
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
        CreateExpenseDTO createExpenseDTO = new CreateExpenseDTO();
        createExpenseDTO.setAmount(BigDecimal.valueOf(50.00));
        createExpenseDTO.setDescription("Dinner");
        createExpenseDTO.setCategoryId(1L);

        when(jpaExpenseRepository.save(any(Expense.class))).thenReturn(expense);

        ExpenseResponseDTO result = expenseService.createExpense(createExpenseDTO);

        assertNotNull(result);
        assertEquals("Dinner", result.getDescription());
        verify(jpaExpenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void testUpdateExpense_Found() {
        CreateExpenseDTO updateExpenseDTO = new CreateExpenseDTO();
        updateExpenseDTO.setAmount(BigDecimal.valueOf(100.00));
        updateExpenseDTO.setDescription("Updated Dinner");
        updateExpenseDTO.setCategoryId(1L);

        Expense updatedExpense = new Expense();
        updatedExpense.setId(1L);
        updatedExpense.setAmount(updateExpenseDTO.getAmount());
        updatedExpense.setDescription(updateExpenseDTO.getDescription());
        updatedExpense.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        when(jpaExpenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        when(jpaExpenseRepository.save(any(Expense.class))).thenReturn(updatedExpense);

        ExpenseResponseDTO result = expenseService.updateExpense(1L, updateExpenseDTO);
        assertNotNull(result);
        assertEquals("Updated Dinner", result.getDescription());
        assertEquals(BigDecimal.valueOf(100.00), result.getAmount());
        verify(jpaExpenseRepository, times(1)).findById(1L);
        verify(jpaExpenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void testUpdateExpense_NotFound() {
        CreateExpenseDTO updateExpenseDTO = new CreateExpenseDTO();
        updateExpenseDTO.setAmount(BigDecimal.valueOf(100.00));
        updateExpenseDTO.setDescription("Updated Dinner");
        updateExpenseDTO.setCategoryId(1L);

        when(jpaExpenseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ExpenseNotFoundException.class, () -> expenseService.updateExpense(1L, updateExpenseDTO));
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

 */
