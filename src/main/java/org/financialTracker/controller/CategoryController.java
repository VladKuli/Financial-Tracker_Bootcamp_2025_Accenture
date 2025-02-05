package org.financialTracker.controller;

import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.CategoryDTO;
import org.financialTracker.mapper.CategoryMapper;
import org.financialTracker.model.Category;
import org.financialTracker.service.CategoryService;
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

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    // CategoryService is responsible for handling the business logic related to category operations.
    // It is injected via constructor to interact with the underlying data layer (e.g., database).
    private final CategoryService categoryService;

    // CategoryMapper is used to convert between Category entities and CategoryDTOs.
    // It helps in mapping the data that is returned from the service layer to a suitable response format (DTO).
    private final CategoryMapper categoryMapper;

    // POST endpoint to create a new category
    @PostMapping("/add")
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.addCategory(category);
        CategoryDTO categoryDTO = categoryMapper.toDTO(savedCategory);
        return ResponseEntity.ok(categoryDTO);
    }

    // GET endpoint to retrieve all categories
    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategories(
            @RequestParam(required = false) String title,   // Optional filter by category title
            @RequestParam(required = false) String icon) {  // Optional filter by category icon
        // Get filtered categories based on request parametersList<Category> categories = categoryService.getCategoriesByFilter(title, icon);
        List<Category> categories = categoryService.getCategoriesByFilter(title, icon);
        //List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = categoryMapper.toDTOList(categories);
        return ResponseEntity.ok(categoryDTOs);
    }

    // GET endpoint to retrieve a category by its ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        CategoryDTO categoryDTO = categoryMapper.toDTO(category);
        return ResponseEntity.ok(categoryDTO);
    }

    // PUT endpoint to update an existing category by its ID
//    @PutMapping("/{id}")
//    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody Category category) {
//       Category updatedCategory = categoryService.updateCategory(category.getId(), category);
//       CategoryDTO categoryDTO = categoryMapper.toDTO(updatedCategory);
//       return ResponseEntity.ok(CategoryDTO);
//  }

    // PUT endpoint to update an existing category (using /update and /{id})
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        CategoryDTO categoryDTO = categoryMapper.toDTO(updatedCategory);
        return ResponseEntity.ok(categoryDTO);
    }

    // DELETE endpoint to delete a category by its ID
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
//        categoryService.deleteCategory(category.getId());
//        return ResponseEntity.ok("Category deleted successfully");
//    }

    // DELETE endpoint to delete a category (using /delete and /{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category deleted successfully");
    }

}
