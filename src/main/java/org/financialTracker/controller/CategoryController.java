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
        return ResponseEntity.ok(categoryService.getCategoriesByFilter(title));
    }

    // GET endpoint to retrieve a category by its ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    // POST endpoint to create a new category
    @PostMapping("/add")
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CreateCategoryDTO createCategoryDTO) {
        return ResponseEntity.ok(categoryService.createCategory(createCategoryDTO));
    }

    // PUT endpoint to update an existing category (using /update and /{id})
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.updateCategory(id, category));
    }

    // DELETE endpoint to delete a category (using /delete and /{id})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Deleted category with id " + id);
    }

}
