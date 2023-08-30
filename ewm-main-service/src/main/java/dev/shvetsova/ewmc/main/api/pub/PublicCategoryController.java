package dev.shvetsova.ewmc.main.api.pub;

import dev.shvetsova.ewmc.main.dto.category.CategoryDto;
import dev.shvetsova.ewmc.main.service.category.CategoryService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static dev.shvetsova.ewmc.main.utils.Constants.FROM;
import static dev.shvetsova.ewmc.main.utils.Constants.PAGE_SIZE;


@RestController
//@RequestMapping(path = "/")
@Slf4j
@RequiredArgsConstructor
@Validated
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(
            @PositiveOrZero @RequestParam(value = "from", defaultValue = FROM) Integer from,
            @Positive @RequestParam(value = "size", defaultValue = PAGE_SIZE) Integer size
    ) {
        log.debug("Request received GET /categories?from={}&size={}", from, size);
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategory(@PathVariable long catId) {
        log.debug("Request received GET /categories/{}", catId);
        return categoryService.getCategory(catId);
    }
}
