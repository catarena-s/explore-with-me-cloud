package dev.shvetsova.ewmc.main.service.category;

import dev.shvetsova.ewmc.common.dto.category.CategoryDto;
import dev.shvetsova.ewmc.common.dto.category.NewCategoryDto;
import dev.shvetsova.ewmc.common.exception.ConflictException;
import dev.shvetsova.ewmc.common.exception.NotFoundException;
import dev.shvetsova.ewmc.main.mapper.CategoryMapper;
import dev.shvetsova.ewmc.main.model.Category;
import dev.shvetsova.ewmc.main.repository.CategoryRepository;
import dev.shvetsova.ewmc.main.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static dev.shvetsova.ewmc.common.Constants.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public Category findCategoryById(long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(CATEGORY_WITH_ID_D_WAS_NOT_FOUND, catId),
                        THE_REQUIRED_OBJECT_WAS_NOT_FOUND));
    }

    @Override
    public CategoryDto saveCategory(NewCategoryDto body) {
        try {
            final Category category = categoryRepository.save(CategoryMapper.fromDto(body));
            return CategoryMapper.toDto(category);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(
                    String.format("Category with name='%s' already exists", body.getName()),
                    INTEGRITY_CONSTRAINT_HAS_BEEN_VIOLATED);
        }
    }

    @Override
    public void delete(long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException(
                    String.format(CATEGORY_WITH_ID_D_WAS_NOT_FOUND, catId),
                    THE_REQUIRED_OBJECT_WAS_NOT_FOUND);
        }

        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException(
                    String.format("The category id=%d is not empty", catId),
                    FOR_THE_REQUESTED_OPERATION_THE_CONDITIONS_ARE_NOT_MET);
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto body, long catId) {
        if (categoryRepository.existsByIdNotAndName(catId, body.getName())) {
            throw new ConflictException(
                    String.format("Another category already exists with name = '%s'", body.getName()),
                    INTEGRITY_CONSTRAINT_HAS_BEEN_VIOLATED
            );
        }
        final Category category = findCategoryById(catId);
        category.setName(body.getName());
        final Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.toDto(updatedCategory);
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        final PageRequest page = PageRequest.of(from / size, size);
        final List<Category> categories = categoryRepository.findAll(page).getContent();
        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(long catId) {
        final Category category = findCategoryById(catId);
        return CategoryMapper.toDto(category);
    }
}
