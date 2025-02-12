package org.financialTracker.service;

import lombok.RequiredArgsConstructor;
import org.financialTracker.dto.response.CategoryResponseDTO;
import org.financialTracker.dto.request.CreateCategoryDTO;
import org.financialTracker.exception.CategoryNotFoundException;
import org.financialTracker.mapper.CategoryMapper;
import org.financialTracker.model.Category;
import org.financialTracker.model.Transaction;
import org.financialTracker.repository.JpaCategoryRepository;
import org.financialTracker.repository.JpaTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final JpaCategoryRepository jpaCategoryRepository;
    private final JpaTransactionRepository jpaTransactionRepository;

    public List<CategoryResponseDTO> getCategoriesByTitle(String title) {
        // Get filtered categories based on request parameters
        List<Category> categories = jpaCategoryRepository.findCategoriesByFilter(title);
        return CategoryMapper.toDTOList(categories);
    }

    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = jpaCategoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category '" + id + "' not found"));

        return CategoryMapper.toDTO(category);
    }

    public CategoryResponseDTO createCategory(CreateCategoryDTO createCategoryDTO) {

        Category newCategory = new Category();
        newCategory.setTitle(createCategoryDTO.getTitle());
        newCategory.setDescription(createCategoryDTO.getDescription());
        newCategory.setIcon(createCategoryDTO.getIcon());
        jpaCategoryRepository.save(newCategory);

        return CategoryMapper.toDTO(newCategory);
    }

    public CategoryResponseDTO updateCategory(Long id, Category category) {
        Category updatedCategory = jpaCategoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category '" + id + "' not found"));

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

        List<Transaction> expens = jpaTransactionRepository.findByCategoryId(id);

        for (Transaction transaction : expens) {
            transaction.setCategory(null);
            jpaTransactionRepository.save(transaction);
        }

        jpaCategoryRepository.deleteById(id);
    }
}
