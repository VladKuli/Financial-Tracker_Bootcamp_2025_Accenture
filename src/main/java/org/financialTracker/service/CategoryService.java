package org.financialTracker.service;

import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.CategoryDTO;
import org.financialTracker.exception.CategoryNotFoundException;
import org.financialTracker.mapper.CategoryMapper;
import org.financialTracker.model.Category;
import org.financialTracker.repository.JpaCategoryRepository;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final JpaCategoryRepository jpaCategoryRepository;

    public List<CategoryDTO> getCategoriesByFilter(String title) {
        // Get filtered categories based on request parameters
        List<Category> categories = jpaCategoryRepository.findCategoriesByFilter(title);
        return CategoryMapper.toDTOList(categories);
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = jpaCategoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Category '" + id + "' not found")
        );

        return CategoryMapper.toDTO(category);
    }

    public CategoryDTO createCategory(Category category) {
        Category savedCategory = jpaCategoryRepository.save(category);
        return CategoryMapper.toDTO(savedCategory);
    }

    public CategoryDTO updateCategory(Long id, Category category) {
        Category updatedCategory = jpaCategoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Category '" + id + "' not found")
        );

        updatedCategory.setTitle(category.getTitle());
        updatedCategory.setIcon(category.getIcon());
        updatedCategory.setDescription(category.getDescription());
        jpaCategoryRepository.save(updatedCategory);
        
        return CategoryMapper.toDTO(updatedCategory);
    }

    public void deleteCategory(Long id) {
        if (!jpaCategoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Category '" + id + "' not found");
        }
        jpaCategoryRepository.deleteById(id);
    }
}
