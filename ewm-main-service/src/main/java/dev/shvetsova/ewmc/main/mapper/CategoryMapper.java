package dev.shvetsova.ewmc.main.mapper;

import dev.shvetsova.ewmc.main.dto.category.CategoryDto;
import dev.shvetsova.ewmc.main.dto.category.NewCategoryDto;
import dev.shvetsova.ewmc.main.model.Category;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {
    public static Category fromDto(NewCategoryDto dto) {
        return Category.builder()
                .name(dto.getName())
                .build();
    }

    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
