package dev.shvetsova.ewmc.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Данные для добавления новой категории
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank(message = "Category name cannot be empty or null")
    @Size(max = 50, message = "size must be between 0 and 50")
    private String name;
}
