package dev.shvetsova.ewmc.dto.category;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Категория
 */
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Category name cannot be empty or null")
    @Size(min = 1, max = 50, message = "size must be between 1 and 50")
    private String name;

    public CategoryDto(Long id,
                       @NotBlank(message = "Category name cannot be empty or null")
                       @Size(min = 1, max = 50, message = "size must be between 1 and 50") String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static CategoryDtoBuilder builder() {
        return new CategoryDtoBuilder();
    }

    public void setName(@NotBlank(message = "Category name cannot be empty or null")
                        @Size(min = 1, max = 50, message = "size must be between 1 and 50") String name) {
        this.name = name;
    }

    public static class CategoryDtoBuilder {
        private Long id;
        @NotBlank(message = "Category name cannot be empty or null")
        @Size(min = 1, max = 50, message = "size must be between 1 and 50")
        private String name;

        CategoryDtoBuilder() {
        }

        public CategoryDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CategoryDtoBuilder name(@NotBlank(message = "Category name cannot be empty or null")
                                       @Size(min = 1, max = 50, message = "size must be between 1 and 50") String name) {
            this.name = name;
            return this;
        }

        public CategoryDto build() {
            return new CategoryDto(this.id, this.name);
        }
    }
}
