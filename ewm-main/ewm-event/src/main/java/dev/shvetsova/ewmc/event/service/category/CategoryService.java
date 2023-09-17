package dev.shvetsova.ewmc.event.service.category;

import dev.shvetsova.ewmc.dto.category.CategoryDto;
import dev.shvetsova.ewmc.dto.category.NewCategoryDto;
import dev.shvetsova.ewmc.event.model.Category;

import java.util.List;

public interface CategoryService {
    /**
     * @param body
     * @return
     */
    CategoryDto saveCategory(NewCategoryDto body);

    /**
     * @param body
     * @param catId
     * @return
     */
    CategoryDto updateCategory(CategoryDto body, long catId);

    /**
     * Удаление категории(с категорией не должно быть связано ни одного события.)
     *
     * @param catId id категории
     */
    void delete(long catId);

    /**
     * Получение категорий
     *
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора default: 0
     * @param size количество категорий в наборе default: 10
     * @return В случае, если по заданным фильтрам не найдено ни одной категории, возвращает пустой список
     */
    List<CategoryDto> getCategories(Integer from, Integer size);

    /**
     * Получение информации о категории по её идентификатору
     *
     * @param catId id категории
     * @return В случае, если категории с заданным id не найдено, возвращает статус код 404
     */
    CategoryDto getCategory(long catId);

    Category findCategoryById(long catId);
}
