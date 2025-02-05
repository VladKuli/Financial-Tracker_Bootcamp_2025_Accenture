package org.financialTracker.controller;

import org.financialTracker.dto.ExpenseDTO;
import org.financialTracker.model.Expense;
import org.financialTracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    // Injects the ExpenseService into the controller to interact with the business logic layer.
    // It is injected via constructor to interact with the underlying data layer (e.g., database).
    private final ExpenseService expenseService;

    // GET endpoint to retrieve all expenses
    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses(
            @RequestParam(required = false) BigDecimal amount,  // Optional filter by amount
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,  // Optional filter by date
            @RequestParam(required = false) String categoryTitle) {  // Optional filter by category
        // Get filtered expenses based on request parameters
        List<ExpenseDTO> expensesDTO = expenseService.getExpensesByFilter(amount, date, categoryTitle);
        return ResponseEntity.ok(expensesDTO);
    }

    // GET endpoint to retrieve a specific expense by its ID
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long id) {
        ExpenseDTO expenseDTO = expenseService.getExpenseById(id);
        return ResponseEntity.ok(expenseDTO);
    }

    // POST endpoint to create/add a new expense
    @PostMapping("/add")
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody Expense expense) {
        ExpenseDTO expenseDTO = expenseService.createExpense(expense);
        return ResponseEntity.ok(expenseDTO);
    }

    // PUT endpoint to update an existing expense (using /update and /{id})
    @PutMapping("/update/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        ExpenseDTO updatedExpenseDTO = expenseService.updateExpense(id, expense);
        if (updatedExpenseDTO != null) {
            return ResponseEntity.ok(updatedExpenseDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DELETE endpoint to delete an expense (using /delete and /{id})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Expense deleted successfully");
    }

}
