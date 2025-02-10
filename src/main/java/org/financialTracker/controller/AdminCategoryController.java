package org.financialTracker.controller;

import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.request.CreateCategoryDTO;
import org.financialTracker.dto.response.CategoryResponseDTO;
import org.financialTracker.model.Category;
import org.financialTracker.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

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
