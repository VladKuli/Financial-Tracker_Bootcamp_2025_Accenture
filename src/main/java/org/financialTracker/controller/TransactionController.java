package org.financialTracker.controller;

import jakarta.security.auth.message.AuthException;
import org.financialTracker.dto.request.AdviceRequest;
import org.financialTracker.dto.request.CreateTransactionDTO;
import org.financialTracker.dto.request.UpdateTransactionDTO;
import org.financialTracker.dto.response.CategoryExpenseDTO;
import org.financialTracker.dto.response.TransactionResponseDTO;
import org.financialTracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    // Injects the TransactionService into the controller to interact with the business logic layer.
    // It is injected via constructor to interact with the underlying data layer (e.g., database).
    private final TransactionService transactionService;

    // GET endpoint to retrieve all USER transactions
    @GetMapping("/all")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(
//            @RequestParam(required = false) BigDecimal amount,  // Optional filter by amount
//            @RequestParam(required = false) Date date,  // Optional filter by date
//            @RequestParam(required = false) String categoryTitle
    ) throws AuthException {  // Optional filter by category
        // Get filtered transactions based on request parameters
        return ResponseEntity.ok(transactionService.getTransactionsByUser());
    }

    // GET endpoint to retrieve a specific USER transaction by its ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable Long id) throws AuthException {
        return ResponseEntity.ok(transactionService.getTransactionsByIdAndUser(id));

    }

    @GetMapping("/monthly")
    public ResponseEntity<List<TransactionResponseDTO>> getMonthlyTransactions() throws AuthException {
        return ResponseEntity.ok(transactionService.getMonthlyTransactions());
    }

    @GetMapping("/expenses")
    public ResponseEntity<List<TransactionResponseDTO>> getExpenses() throws AuthException {
        return ResponseEntity.ok(transactionService.getExpensesByUser());
    }

    @GetMapping("/incomes")
    public ResponseEntity<List<TransactionResponseDTO>> getIncomes() throws AuthException {
        return ResponseEntity.ok(transactionService.getIncomesByUser());
    }

    @GetMapping("/incomes/monthly/total")
    public ResponseEntity<BigDecimal> getTotalIncomes() throws AuthException {
        return ResponseEntity.ok(transactionService.getTotalMonthlyIncomes());
    }

    @GetMapping("/expenses/monthly")
    public ResponseEntity<List<TransactionResponseDTO>> getMonthlyExpenses() throws AuthException {
        return ResponseEntity.ok(transactionService.getMonthlyExpenses());
    }

    @GetMapping("/expenses/monthly/total")
    public ResponseEntity<BigDecimal> getTotalMonthlyExpenses() throws AuthException {
        return ResponseEntity.ok(transactionService.getTotalMonthlyExpenses());
    }

    @GetMapping("/total-category")
    public ResponseEntity<List<CategoryExpenseDTO>> getTotalExpensesByCategory() throws AuthException {
        return ResponseEntity.ok(transactionService.getTotalExpensesByCategory());
    }

    // POST endpoint to create/add a new transaction
    @PostMapping("/add")
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody CreateTransactionDTO createTransactionDTO) throws AuthException {
        return ResponseEntity.ok(transactionService.createTransaction(createTransactionDTO));
    }

    // PUT endpoint to update an existing transaction (using /update and /{id})
    @PutMapping("/update/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(@PathVariable Long id, @RequestBody UpdateTransactionDTO updateTransactionDTO) throws AuthException {
        return ResponseEntity.ok(transactionService.updateTransaction(id, updateTransactionDTO));
    }

    // DELETE endpoint to delete an transaction (using /delete and /{id})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) throws AuthException {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Deleted transaction with id " + id);
    }

}
