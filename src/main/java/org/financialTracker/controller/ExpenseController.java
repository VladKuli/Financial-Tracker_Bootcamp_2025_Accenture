package org.financialTracker.controller;

import org.financialTracker.dto.ExpenseDTO;
import org.financialTracker.mapper.ExpenseMapper;
import org.financialTracker.model.Expense;
import org.financialTracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    // Injects the ExpenseService into the controller to interact with the business logic layer.
    // It is injected via constructor to interact with the underlying data layer (e.g., database).
    private final ExpenseService expenseService;

    // Injects the ExpenseMapper into the controller to convert between Expense entity and ExpenseDTO.
    // It helps in mapping the data that is returned from the service layer to a suitable response format (DTO).
    private final ExpenseMapper expenseMapper;

    // POST endpoint to create/add a new expense
    @PostMapping("/add")
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody Expense expense) {
        Expense savedExpense = expenseService.addExpense(expense);
        ExpenseDTO expenseDTO = expenseMapper.toDTO(savedExpense);
        return ResponseEntity.ok(expenseDTO);
    }

    // GET endpoint to retrieve all expenses
    @GetMapping("/all")
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses(
            @RequestParam(required = false) BigDecimal amount,  // Optional filter by amount
            @RequestParam(required = false) String date,  // Optional filter by date
            @RequestParam(required = false) String category) {  // Optional filter by category
        // Get filtered expenses based on request parameters
        List<Expense> expenses = expenseService.getExpensesByFilter(amount, date, category);
        // List<Expense> expenses = expenseService.getAllExpenses();
        List<ExpenseDTO> expenseDTOs = expenseMapper.toDTOList(expenses);
        return ResponseEntity.ok(expenseDTOs);
    }

    // GET endpoint to retrieve a specific expense by its ID
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable Long id) {
        Expense expense = expenseService.getExpenseById(id);
        ExpenseDTO expenseDTO = expenseMapper.toDTO(expense);
        return ResponseEntity.ok(expenseDTO);
    }

 //   @PutMapping("/{id}")
 //   public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
 //       Expense updatedExpense = expenseService.updateExpense(id, expense);
 //       ExpenseDTO expenseDTO = expenseMapper.toDTO(updatedExpense);
 //       return ResponseEntity.ok(ExpenseDTO);
 //   }

    // PUT endpoint to update an existing expense (using /update and /{id})
    @PutMapping("/update/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        // Update the expense using the provided ID and expense object
        Expense updatedExpense = expenseService.updateExpense(id, expense);
        ExpenseDTO expenseDTO = expenseMapper.toDTO(updatedExpense);
        return ResponseEntity.ok(expenseDTO);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
//        expenseService.deleteExpense(id);
//        return ResponseEntity.ok("Expense deleted successfully");
//    }

    // DELETE endpoint to delete an expense (using /delete and /{id})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Expense deleted successfully");
    }

}
