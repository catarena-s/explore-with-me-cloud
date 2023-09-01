
package dev.shvetsova.ewmc.common.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Подборка событий
 */
@Data
@Builder
public class NewCompilationDto {
    @NotBlank(message = "Title cannot be null or empty")
    @Size(min = 1, max = 50,message = "size must be between 1 and 50")
    private String title;
    private boolean pinned = false;
    private List<Long> events;
}
