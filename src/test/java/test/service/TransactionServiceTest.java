package test.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.security.auth.message.AuthException;
import org.financialTracker.dto.request.CreateTransactionDTO;
import org.financialTracker.dto.request.UpdateTransactionDTO;
import org.financialTracker.dto.response.TransactionResponseDTO;
import org.financialTracker.dto.response.UserResponseDTO;
import org.financialTracker.exception.TransactionNotFoundException;
import org.financialTracker.model.Transaction;
import org.financialTracker.model.TransactionType;
import org.financialTracker.repository.JpaCategoryRepository;
import org.financialTracker.repository.JpaTransactionRepository;
import org.financialTracker.repository.JpaUserRepository;
import org.financialTracker.service.AuthService;
import org.financialTracker.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private JpaTransactionRepository transactionRepository;

    @Mock
    private JpaUserRepository userRepository;

    @Mock
    private JpaCategoryRepository categoryRepository;

    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTransactionsByUser() throws AuthException {
        UserResponseDTO user = new UserResponseDTO();
        user.setUsername("testUser");
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(transactionRepository.findByUser_Username("testUser")).thenReturn(Collections.emptyList());

        List<TransactionResponseDTO> transactions = transactionService.getTransactionsByUser();

        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }

    @Test
    void testCreateTransaction() throws AuthException {
        UserResponseDTO user = new UserResponseDTO();
        user.setUsername("testUser");
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(new org.financialTracker.model.User()));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(new org.financialTracker.model.Category()));

        CreateTransactionDTO dto = new CreateTransactionDTO();
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setTransactionType(TransactionType.INCOME);
        dto.setCategoryId(1L);
        dto.setDescription("Test Transaction");

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(dto.getAmount());
        newTransaction.setDescription(dto.getDescription());
        newTransaction.setTransactionType(dto.getTransactionType());

        when(transactionRepository.save(any(Transaction.class))).thenReturn(newTransaction);

        TransactionResponseDTO response = transactionService.createTransaction(dto);

        assertNotNull(response);
        assertEquals("Test Transaction", response.getDescription());
    }

    @Test
    void testUpdateTransactionNotFound() throws AuthException {
        UserResponseDTO user = new UserResponseDTO();
        user.setUsername("testUser");
        when(authService.getAuthenticatedUser()).thenReturn(user);
        when(transactionRepository.findTransactionByIdAndUser_Username(anyLong(), anyString())).thenReturn(Optional.empty());

        UpdateTransactionDTO dto = new UpdateTransactionDTO();
        dto.setAmount(BigDecimal.valueOf(50));
        dto.setCategoryId(1L);
        dto.setDescription("Updated Transaction");

        assertThrows(TransactionNotFoundException.class, () -> transactionService.updateTransaction(1L, dto));
    }
}
