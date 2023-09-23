package dev.shvetsova.ewmc.dto.category;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Категория
 */
public class CategoryDto {
    private Long id;

    public String getName() {
        return name;
    }

    @NotBlank(message = "Category name cannot be empty or null")
    @Size(min = 1, max = 50, message = "size must be between 1 and 50")
    private String name;

    public CategoryDto(Long id, @NotBlank(message = "Category name cannot be empty or null") @Size(min = 1, max = 50, message = "size must be between 1 and 50") String name) {
        this.id = id;
        this.name = name;
    }

    public static CategoryDtoBuilder builder() {
        return new CategoryDtoBuilder();
    }

    public Long getId() {
        return this.id;
    }

//    public @NotBlank(message = "Category name cannot be empty or null")
//    @Size(min = 1, max = 50, message = "size must be between 1 and 50") String getName() {
//        return this.name;
//    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(@NotBlank(message = "Category name cannot be empty or null") @Size(min = 1, max = 50, message = "size must be between 1 and 50") String name) {
        this.name = name;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CategoryDto;
    }

    public static class CategoryDtoBuilder {
        private Long id;
        private @NotBlank(message = "Category name cannot be empty or null") @Size(min = 1, max = 50, message = "size must be between 1 and 50") String name;

        CategoryDtoBuilder() {
        }

        public CategoryDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CategoryDtoBuilder name(@NotBlank(message = "Category name cannot be empty or null") @Size(min = 1, max = 50, message = "size must be between 1 and 50") String name) {
            this.name = name;
            return this;
        }

        public CategoryDto build() {
            return new CategoryDto(this.id, this.name);
        }

        public String toString() {
            return "CategoryDto.CategoryDtoBuilder(id=" + this.id + ", name=" + this.name + ")";
        }
    }
}
