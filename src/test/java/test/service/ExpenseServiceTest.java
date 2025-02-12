package test.service;
/*
import org.financialTracker.dto.request.CreateTransactionDTO;
import org.financialTracker.dto.response.TransactionResponseDTO;
import org.financialTracker.exception.TransactionNotFoundException;
import org.financialTracker.mapper.TransactionMapper;
import org.financialTracker.model.Transaction;
import org.financialTracker.repository.JpaTransactionRepository;
import org.financialTracker.service.TransactionService;
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
class TransactionServiceTest {

    @Mock
    private JpaTransactionRepository jpaTransactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;
    private TransactionResponseDTO transactionDTO;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(BigDecimal.valueOf(50.00));
        transaction.setDescription("Dinner");
        transaction.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        transactionDTO = TransactionMapper.toDTO(transaction);
    }

    @Test
    void testGetTransactionsByUser() {
        when(jpaTransactionRepository.getTransactionsByUser()).thenReturn(List.of(transaction));
        List<TransactionResponseDTO> result = transactionService.getTransactionsByUser();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jpaTransactionRepository, times(1)).getTransactionsByUser();
    }

    @Test
    void testGetTransactionById_Found() {
        when(jpaTransactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        TransactionResponseDTO result = transactionService.getTransactionById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(jpaTransactionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTransactionById_NotFound() {
        when(jpaTransactionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(1L));
        verify(jpaTransactionRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateTransaction() {
        CreateTransactionDTO createTransactionDTO = new CreateTransactionDTO();
        createTransactionDTO.setAmount(BigDecimal.valueOf(50.00));
        createTransactionDTO.setDescription("Dinner");
        createTransactionDTO.setCategoryId(1L);

        when(jpaTransactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionResponseDTO result = transactionService.createTransaction(createTransactionDTO);

        assertNotNull(result);
        assertEquals("Dinner", result.getDescription());
        verify(jpaTransactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testUpdateTransaction_Found() {
        CreateTransactionDTO updateTransactionDTO = new CreateTransactionDTO();
        updateTransactionDTO.setAmount(BigDecimal.valueOf(100.00));
        updateTransactionDTO.setDescription("Updated Dinner");
        updateTransactionDTO.setCategoryId(1L);

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setId(1L);
        updatedTransaction.setAmount(updateTransactionDTO.getAmount());
        updatedTransaction.setDescription(updateTransactionDTO.getDescription());
        updatedTransaction.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        when(jpaTransactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(jpaTransactionRepository.save(any(Transaction.class))).thenReturn(updatedTransaction);

        TransactionResponseDTO result = transactionService.updateTransaction(1L, updateTransactionDTO);
        assertNotNull(result);
        assertEquals("Updated Dinner", result.getDescription());
        assertEquals(BigDecimal.valueOf(100.00), result.getAmount());
        verify(jpaTransactionRepository, times(1)).findById(1L);
        verify(jpaTransactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testUpdateTransaction_NotFound() {
        CreateTransactionDTO updateTransactionDTO = new CreateTransactionDTO();
        updateTransactionDTO.setAmount(BigDecimal.valueOf(100.00));
        updateTransactionDTO.setDescription("Updated Dinner");
        updateTransactionDTO.setCategoryId(1L);

        when(jpaTransactionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> transactionService.updateTransaction(1L, updateTransactionDTO));
        verify(jpaTransactionRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteTransaction_Found() {
        when(jpaTransactionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(jpaTransactionRepository).deleteById(1L);

        assertDoesNotThrow(() -> transactionService.deleteTransaction(1L));
        verify(jpaTransactionRepository, times(1)).existsById(1L);
        verify(jpaTransactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTransaction_NotFound() {
        when(jpaTransactionRepository.existsById(1L)).thenReturn(false);
        assertThrows(TransactionNotFoundException.class, () -> transactionService.deleteTransaction(1L));
        verify(jpaTransactionRepository, times(1)).existsById(1L);
    }
}

 */
