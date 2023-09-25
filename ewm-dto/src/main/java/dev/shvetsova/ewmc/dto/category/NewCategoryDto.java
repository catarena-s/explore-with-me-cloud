package dev.shvetsova.ewmc.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * Данные для добавления новой категории
 */

public class NewCategoryDto {
    @NotBlank(message = "Category name cannot be empty or null")
    @Size(max = 50, message = "size must be between 0 and 50")
    private String name;

    public NewCategoryDto(@NotBlank(message = "Category name cannot be empty or null") @Size(max = 50, message = "size must be between 0 and 50") String name) {
        this.name = name;
    }

    public NewCategoryDto() {
    }

    public static NewCategoryDtoBuilder builder() {
        return new NewCategoryDtoBuilder();
    }

    public @NotBlank(message = "Category name cannot be empty or null") @Size(max = 50, message = "size must be between 0 and 50") String getName() {
        return this.name;
    }

    public void setName(@NotBlank(message = "Category name cannot be empty or null") @Size(max = 50, message = "size must be between 0 and 50") String name) {
        this.name = name;
    }

    public static class NewCategoryDtoBuilder {
        private @NotBlank(message = "Category name cannot be empty or null") @Size(max = 50, message = "size must be between 0 and 50") String name;

        NewCategoryDtoBuilder() {
        }

        public NewCategoryDtoBuilder name(@NotBlank(message = "Category name cannot be empty or null") @Size(max = 50, message = "size must be between 0 and 50") String name) {
            this.name = name;
            return this;
        }

        public NewCategoryDto build() {
            return new NewCategoryDto(this.name);
        }
    }
}
