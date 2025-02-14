package test.service;

import org.financialTracker.dto.request.CreateCategoryDTO;
import org.financialTracker.dto.response.CategoryResponseDTO;
import org.financialTracker.exception.CategoryNotFoundException;
import org.financialTracker.mapper.CategoryMapper;
import org.financialTracker.model.Category;
import org.financialTracker.model.Transaction;
import org.financialTracker.repository.JpaCategoryRepository;
import org.financialTracker.repository.JpaTransactionRepository;
import org.financialTracker.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private JpaCategoryRepository jpaCategoryRepository;

    @Mock
    private JpaTransactionRepository jpaTransactionRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setTitle("Food");
        category.setDescription("Food and groceries");
        category.setIcon("food_icon");
    }

    @Test
    void testGetCategoryById_Success() {
        when(jpaCategoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponseDTO response = categoryService.getCategoryById(1L);

        assertNotNull(response);
        assertEquals("Food", response.getTitle());
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(jpaCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1L));
    }

    @Test
    void testCreateCategory() {
        CreateCategoryDTO dto = new CreateCategoryDTO("Transport", "Public transport expenses", "bus_icon");
        Category newCategory = new Category();
        newCategory.setTitle(dto.getTitle());
        newCategory.setDescription(dto.getDescription());
        newCategory.setIcon(dto.getIcon());

        when(jpaCategoryRepository.save(any(Category.class))).thenReturn(newCategory);

        CategoryResponseDTO response = categoryService.createCategory(dto);

        assertNotNull(response);
        assertEquals("Transport", response.getTitle());
    }

    @Test
    void testUpdateCategory_Success() {
        Category updatedCategory = new Category();
        updatedCategory.setTitle("Updated Food");
        updatedCategory.setDescription("Updated Description");
        updatedCategory.setIcon("updated_icon");

        when(jpaCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(jpaCategoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        CategoryResponseDTO response = categoryService.updateCategory(1L, updatedCategory);

        assertNotNull(response);
        assertEquals("Updated Food", response.getTitle());
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(jpaCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(1L, category));
    }

    @Test
    void testDeleteCategory_Success() {
        when(jpaCategoryRepository.existsById(1L)).thenReturn(true);
        when(jpaTransactionRepository.findByCategoryId(1L)).thenReturn(List.of());

        categoryService.deleteCategory(1L);

        verify(jpaCategoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(jpaCategoryRepository.existsById(1L)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(1L));
    }
}
