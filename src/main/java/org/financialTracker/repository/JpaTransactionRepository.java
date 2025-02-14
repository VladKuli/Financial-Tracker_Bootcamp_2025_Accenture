package org.financialTracker.repository;

import org.financialTracker.dto.request.AdviceRequest;
import org.financialTracker.dto.response.UserResponseDTO;
import org.financialTracker.model.Transaction;
import org.financialTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaTransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser_Username(String username);

    Optional<Transaction> findTransactionByIdAndUser_Username(Long id, String username);

    @Query("SELECT e FROM Transaction e WHERE e.transactionType = 1 AND e.user.id = :userId")
    List<Transaction> findExpenses(Long userId);

    @Query("SELECT e FROM Transaction e WHERE e.transactionType = 0 AND e.user.id = :userId")
    List<Transaction> findIncomes(Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.date BETWEEN :startDate AND :endDate AND t.user.username = :username")
    List<Transaction> findTransactionsForCurrentMonth(String username, Date startDate, Date endDate);

    @Query("SELECT t FROM Transaction t WHERE t.date BETWEEN :startDate AND :endDate AND t.transactionType = 0 AND t.user.username = :username")
    List<Transaction> findIncomesForCurrentMonth(String username, Date startDate, Date endDate);

    @Query("SELECT t FROM Transaction t WHERE t.date BETWEEN :startDate AND :endDate AND t.transactionType = 1 AND t.user.username = :username")
    List<Transaction> findExpensesForCurrentMonth(String username, Date startDate, Date endDate);

    @Query("SELECT t.category.title, SUM(t.amount * -1) FROM Transaction t WHERE t.transactionType = 1 AND t.user.id = :userId GROUP BY t.category")
    List<Object[]> getTotalAmountByCategory(Long userId);

    List<Transaction> findByCategoryId(Long id);

    @Query("SELECT e FROM Transaction e JOIN e.category c " +
            "WHERE (:amount IS NULL OR e.amount = :amount) " +
            "AND (:date IS NULL OR e.date = :date) " +
            "AND (:categoryTitle IS NULL OR c.title = :categoryTitle)")
    List<Transaction> findTransactionsByFilter(@Param("amount") BigDecimal amount, @Param("date") LocalDate date, @Param("categoryTitle") String categoryTitle);

}