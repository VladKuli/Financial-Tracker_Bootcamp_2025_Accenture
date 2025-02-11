package org.financialTracker.controller;

import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.response.CategoryResponseDTO;
import org.financialTracker.dto.request.CreateCategoryDTO;
import org.financialTracker.model.Category;
import org.financialTracker.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    // CategoryService is responsible for handling the business logic related to category operations.
    // It is injected via constructor to interact with the underlying data layer (e.g., database).
    private final CategoryService categoryService;

    // GET endpoint to retrieve all categories
    @GetMapping("/all")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(
        @RequestParam(required = false) String title) {   // Optional filter by category title
        return ResponseEntity.ok(categoryService.getCategoriesByTitle(title));
    }

    // GET endpoint to retrieve a category by its ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

}
