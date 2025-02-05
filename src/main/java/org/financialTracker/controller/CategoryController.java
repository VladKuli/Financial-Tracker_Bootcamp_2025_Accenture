package org.financialTracker.controller;

import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.CategoryDTO;
import org.financialTracker.mapper.CategoryMapper;
import org.financialTracker.model.Category;
import org.financialTracker.service.CategoryService;
import org.springframework.http.HttpStatus;
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
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories(
        @RequestParam(required = false) String title) {   // Optional filter by category title
        List<CategoryDTO> categoriesDTO = categoryService.getCategoriesByFilter(title);
        return ResponseEntity.ok(categoriesDTO);
    }

    // GET endpoint to retrieve a category by its ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDTO);
    }

    // POST endpoint to create a new category
    @PostMapping("/add")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category) {
        CategoryDTO categoryDTO = categoryService.createCategory(category);
        return ResponseEntity.ok(categoryDTO);
    }

    // PUT endpoint to update an existing category (using /update and /{id})
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {
        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(id, category);
        if (updatedCategoryDTO != null) {
            return ResponseEntity.ok(updatedCategoryDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DELETE endpoint to delete a category (using /delete and /{id})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        boolean isDeleted = categoryService.deleteCategory(id);
        if (isDeleted) {
            return new ResponseEntity<>("Category '" + id + "' deleted successfully", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Category '" + id + "' not found", HttpStatus.NOT_FOUND);
        }
    }

}
