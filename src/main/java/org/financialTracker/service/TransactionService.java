package org.financialTracker.service;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.request.UpdateTransactionDTO;
import org.financialTracker.dto.response.TransactionResponseDTO;
import org.financialTracker.dto.request.CreateTransactionDTO;
import org.financialTracker.dto.response.UserResponseDTO;
import org.financialTracker.exception.CategoryNotFoundException;
import org.financialTracker.exception.TransactionNotFoundException;
import org.financialTracker.exception.UserNotFoundException;
import org.financialTracker.mapper.TransactionMapper;
import org.financialTracker.model.Transaction;
import org.financialTracker.repository.JpaCategoryRepository;
import org.financialTracker.repository.JpaTransactionRepository;
import org.financialTracker.repository.JpaUserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final JpaTransactionRepository jpaTransactionRepository;
    private final AuthService authService;
    private final JpaUserRepository jpaUserRepository;
    private final JpaCategoryRepository jpaCategoryRepository;

    public List<TransactionResponseDTO> getTransactionsByUser() throws AuthException {
        UserResponseDTO currentUser = authService.getAuthenticatedUser();

        return TransactionMapper.toDTOList(jpaTransactionRepository.findByUser_Username(currentUser.getUsername()));
    }

    public TransactionResponseDTO getTransactionsByIdAndUser(Long id) throws AuthException {
        UserResponseDTO currentUser = authService.getAuthenticatedUser();

        Transaction transaction = jpaTransactionRepository.findTransactionByIdAndUser_Username(id, currentUser.getUsername())
                .orElseThrow(
                        () -> new TransactionNotFoundException("Transaction with ID '" + id + "' not found")
                );

        return TransactionMapper.toDTO(transaction);
    }

    public List<TransactionResponseDTO> getMonthlyTransactions() throws AuthException {
        UserResponseDTO currentUser = authService.getAuthenticatedUser();
        LocalDate startDateLocal = LocalDate.now().withDayOfMonth(1);
        LocalDate endDateLocal = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        Date startDate = Date.from(startDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endDateLocal.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return TransactionMapper.toDTOList(jpaTransactionRepository.findTransactionsForCurrentMonth(currentUser.getUsername(), startDate, endDate));
    }

    public BigDecimal getTotalMonthlyTransactions() throws AuthException {
        return getMonthlyTransactions().stream()
                .map(TransactionResponseDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public TransactionResponseDTO createTransaction(CreateTransactionDTO createTransactionDTO) throws AuthException {
        UserResponseDTO currentUser = authService.getAuthenticatedUser();

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(createTransactionDTO.getAmount());
        newTransaction.setDescription(createTransactionDTO.getDescription());
        newTransaction.setCategory(
                jpaCategoryRepository.findById(createTransactionDTO.getCategoryId())
                        .orElseThrow(
                                () -> new CategoryNotFoundException("Category with ID '" + createTransactionDTO.getCategoryId() + "' not found")
                        )
        );
        newTransaction.setUser(jpaUserRepository.findByUsername(currentUser.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User not found")
        ));
        newTransaction.setTransactionType(createTransactionDTO.getTransactionType());
        jpaTransactionRepository.save(newTransaction);

        return TransactionMapper.toDTO(newTransaction);
    }

    public TransactionResponseDTO updateTransaction(Long id, UpdateTransactionDTO updateTransactionDTO) throws AuthException {
        UserResponseDTO currentUser = authService.getAuthenticatedUser();

        Transaction updatedTransaction = jpaTransactionRepository.findTransactionByIdAndUser_Username(id, currentUser.getUsername())
                .orElseThrow(
                        () -> new TransactionNotFoundException("Transaction with ID '" + id + "' not found")
                );

        updatedTransaction.setAmount(updateTransactionDTO.getAmount());
        updatedTransaction.setDescription(updateTransactionDTO.getDescription());
        updatedTransaction.setCategory(
                jpaCategoryRepository.findById(updateTransactionDTO.getCategoryId())
                        .orElseThrow(
                                () -> new CategoryNotFoundException("Category with ID '" + updateTransactionDTO.getCategoryId() + "' not found")
                        )
        );
        jpaTransactionRepository.save(updatedTransaction);

        return TransactionMapper.toDTO(updatedTransaction);
    }

    public void deleteTransaction(Long id) throws AuthException {
        UserResponseDTO currentUser = authService.getAuthenticatedUser();

        Transaction transaction = jpaTransactionRepository.findTransactionByIdAndUser_Username(id, currentUser.getUsername())
                .orElseThrow(
                    () -> new TransactionNotFoundException("Transaction with ID '" + id + "' not found")
                );

        jpaTransactionRepository.delete(transaction);
    }
}
