package dev.shvetsova.ewmc.event.mapper;

import dev.shvetsova.ewmc.event.dto.category.CategoryDto;
import dev.shvetsova.ewmc.event.dto.category.NewCategoryDto;
import dev.shvetsova.ewmc.event.model.Category;
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
