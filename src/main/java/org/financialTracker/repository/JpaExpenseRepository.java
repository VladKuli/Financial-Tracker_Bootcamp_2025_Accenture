package org.financialTracker.repository;

import org.financialTracker.model.Expense;
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
public interface JpaExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser_Username(String username);

    Optional<Expense> findExpenseByIdAndUser_Username(Long id, String username);

    @Query("SELECT e FROM Expense e WHERE e.date BETWEEN :startDate AND :endDate AND e.user.id = :userId")
    List<Expense> findExpensesForCurrentMonth(Long userId, Date startDate, Date endDate);

    @Query("SELECT e FROM Expense e JOIN e.category c " +
            "WHERE (:amount IS NULL OR e.amount = :amount) " +
            "AND (:date IS NULL OR e.date = :date) " +
            "AND (:categoryTitle IS NULL OR c.title = :categoryTitle)")
    List<Expense> findExpensesByFilter(@Param("amount") BigDecimal amount, @Param("date") LocalDate date, @Param("categoryTitle") String categoryTitle);

}