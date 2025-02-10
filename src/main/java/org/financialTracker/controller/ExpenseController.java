package org.financialTracker.controller;

import jakarta.security.auth.message.AuthException;
import org.financialTracker.dto.request.UpdateExpenseDTO;
import org.financialTracker.dto.response.ExpenseResponseDTO;
import org.financialTracker.dto.request.CreateExpenseDTO;
import org.financialTracker.model.Expense;
import org.financialTracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    // Injects the ExpenseService into the controller to interact with the business logic layer.
    // It is injected via constructor to interact with the underlying data layer (e.g., database).
    private final ExpenseService expenseService;

    // GET endpoint to retrieve all USER expenses
    @GetMapping("/all")
    public ResponseEntity<List<ExpenseResponseDTO>> getAllExpenses() throws AuthException
//            @RequestParam(required = false) BigDecimal amount,  // Optional filter by amount
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,  // Optional filter by date
//            @RequestParam(required = false) String categoryTitle) throws AuthException {  // Optional filter by category
    {
        // Get filtered expenses based on request parameters
        return ResponseEntity.ok(expenseService.getExpensesByUser());
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<ExpenseResponseDTO>> getMonthlyExpenses() throws AuthException {
        return ResponseEntity.ok(expenseService.getMonthlyExpenses());
    }

    @GetMapping("/monthly/total")
    public ResponseEntity<String> getTotalMonthlyExpenses() throws AuthException {
        return ResponseEntity.ok("Total: " + expenseService.getTotalMonthlyExpenses());
    }

    // GET endpoint to retrieve a specific USER expense by its ID
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(@PathVariable Long id) throws AuthException {
        return ResponseEntity.ok(expenseService.getExpensesByIdAndUser(id));

    }

    // POST endpoint to create/add a new expense
    @PostMapping("/add")
    public ResponseEntity<ExpenseResponseDTO> createExpense(@RequestBody CreateExpenseDTO createExpenseDTO) throws AuthException {
        return ResponseEntity.ok(expenseService.createExpense(createExpenseDTO));
    }

    // PUT endpoint to update an existing expense (using /update and /{id})
    @PutMapping("/update/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(@PathVariable Long id, @RequestBody UpdateExpenseDTO updateExpenseDTO) throws AuthException {
        return ResponseEntity.ok(expenseService.updateExpense(id, updateExpenseDTO));
    }

    // DELETE endpoint to delete an expense (using /delete and /{id})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) throws AuthException {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Deleted expense with id " + id);
    }

}
