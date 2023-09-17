package dev.shvetsova.ewmc.event.api;

import dev.shvetsova.ewmc.dto.category.CategoryDto;
import dev.shvetsova.ewmc.event.service.category.CategoryService;
import dev.shvetsova.ewmc.utils.Constants;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/categories")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(
            @PositiveOrZero @RequestParam(value = "from", defaultValue = Constants.FROM) Integer from,
            @Positive @RequestParam(value = "size", defaultValue = Constants.PAGE_SIZE) Integer size
    ) {
        log.debug("Request received GET /categories?from={}&size={}", from, size);
        return categoryService.getCategories(from, size);
    }

    @GetMapping("{catId}")
    public CategoryDto getCategory(@PathVariable long catId) {
        log.debug("Request received GET /categories/{}", catId);
        return categoryService.getCategory(catId);
    }
}
