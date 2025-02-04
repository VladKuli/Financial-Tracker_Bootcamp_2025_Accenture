package org.financialTracker.mapper;

import org.financialTracker.dto.CategoryDTO;
import org.financialTracker.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    // Convert a single Category entity to CategoryDTO
    public static CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDTO(
                category.getId(),
                category.getTitle(),
                category.getIcon()
        );
    }

    // Convert a list of Category entities to a list of CategoryDTOs
    public static List<CategoryDTO> toDTOList(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}

