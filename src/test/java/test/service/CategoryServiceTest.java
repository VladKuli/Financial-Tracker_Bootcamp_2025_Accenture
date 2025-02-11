package test.service;

import org.financialTracker.dto.CategoryDTO;
import org.financialTracker.exception.CategoryNotFoundException;
import org.financialTracker.mapper.CategoryMapper;
import org.financialTracker.model.Category;
import org.financialTracker.repository.JpaCategoryRepository;
import org.financialTracker.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private JpaCategoryRepository jpaCategoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setTitle("Food");
        category.setIcon("icon.png");
        category.setDescription("Food expenses");

        categoryDTO = CategoryMapper.toDTO(category);
    }

    @Test
    void testGetCategoriesByFilter() {
        when(jpaCategoryRepository.findCategoriesByFilter("Food")).thenReturn(List.of(category));
        List<CategoryDTO> result = categoryService.getCategoriesByFilter("Food");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Food", result.get(0).getTitle());
        verify(jpaCategoryRepository, times(1)).findCategoriesByFilter("Food");
    }

    @Test
    void testGetCategoryById_Found() {
        when(jpaCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        CategoryDTO result = categoryService.getCategoryById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Food", result.getTitle());
        verify(jpaCategoryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCategoryById_NotFound() {
        when(jpaCategoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById(1L));
        verify(jpaCategoryRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateCategory() {
        when(jpaCategoryRepository.save(category)).thenReturn(category);
        CategoryDTO result = categoryService.createCategory(category);
        assertNotNull(result);
        assertEquals("Food", result.getTitle());
        verify(jpaCategoryRepository, times(1)).save(category);
    }

    @Test
    void testUpdateCategory_Found() {
        Category updatedCategory = new Category();
        updatedCategory.setTitle("Updated Title");
        updatedCategory.setIcon("updated_icon.png");

        when(jpaCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(jpaCategoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        CategoryDTO result = categoryService.updateCategory(1L, updatedCategory);
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("updated_icon.png", result.getIcon());

        verify(jpaCategoryRepository, times(1)).findById(1L);
        verify(jpaCategoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategory_NotFound() {
        when(jpaCategoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(1L, category));
        verify(jpaCategoryRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteCategory_Found() {
        when(jpaCategoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(jpaCategoryRepository).deleteById(1L);

        assertDoesNotThrow(() -> categoryService.deleteCategory(1L));
        verify(jpaCategoryRepository, times(1)).existsById(1L);
        verify(jpaCategoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCategory_NotFound() {
        when(jpaCategoryRepository.existsById(1L)).thenReturn(false);
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(1L));
        verify(jpaCategoryRepository, times(1)).existsById(1L);
    }
}

