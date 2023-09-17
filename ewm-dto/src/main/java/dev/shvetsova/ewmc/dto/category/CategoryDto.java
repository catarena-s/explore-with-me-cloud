package dev.shvetsova.ewmc.dto.category;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Категория
 */
@Data
@Builder
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    @NotBlank(message = "Category name cannot be empty or null")
    @Size(min = 1, max = 50, message = "size must be between 1 and 50")
    private String name;
}
