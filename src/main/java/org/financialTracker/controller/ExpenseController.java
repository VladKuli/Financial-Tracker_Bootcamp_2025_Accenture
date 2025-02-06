package org.financialTracker.controller;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.financialTracker.dto.ExpenseCreateDTO;
import org.financialTracker.dto.ExpenseDTO;
import org.financialTracker.model.Expense;
import org.financialTracker.model.User;
import org.financialTracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.financialTracker.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
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
    private final UserService userService;

    // GET endpoint to retrieve all expenses
    @GetMapping("/all")
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses(
            @RequestParam String username,
            @RequestParam(required = false) BigDecimal amount,  // Optional filter by amount
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,  // Optional filter by date
            @RequestParam(required = false) String categoryTitle) {  // Optional filter by category
        User currentUser = userService.getCurrentUser(username);
        List<ExpenseDTO> expensesDTO = expenseService.findExpensesByUser(currentUser, amount, date, categoryTitle);
        // Get filtered expenses based on request parameters
        return ResponseEntity.ok(expensesDTO);
    }

    // GET endpoint to retrieve a specific expense by its ID
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    // POST endpoint to create/add a new expense
    @PostMapping("/add")
    public ResponseEntity<ExpenseDTO> createExpense(Authentication authentication, @RequestBody ExpenseCreateDTO expenseCreateDTO) {
        User currentUser = userService.getCurrentUser(authentication);
        return ResponseEntity.ok(expenseService.createExpense(currentUser, expenseCreateDTO));
    }

    // PUT endpoint to update an existing expense (using /update and /{id})
    @PutMapping("/update/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        return ResponseEntity.ok(expenseService.updateExpense(id, expense));
    }

    // DELETE endpoint to delete an expense (using /delete and /{id})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Deleted expense with id " + id);
    }

}
