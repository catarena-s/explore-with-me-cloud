package dev.shvetsova.ewmc.event.api;

import dev.shvetsova.ewmc.dto.category.CategoryDto;
import dev.shvetsova.ewmc.event.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import dev.shvetsova.ewmc.dto.category.NewCategoryDto;


@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto saveCategory(@Valid @RequestBody NewCategoryDto body) {
        log.debug("Request received POST /admin/categories : {}", body);
        return categoryService.saveCategory(body);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto body, @PathVariable long catId) {
        log.debug("Request received DELETE /admin/categories/{}:{}", catId, body);
        return categoryService.updateCategory(body, catId);
    }

    /**
     * с категорией не должно быть связано ни одного события.
     * @param catId
     */
    @DeleteMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        log.debug("Request received DELETE /admin/categories/{}", catId);
        categoryService.delete(catId);
    }

}
