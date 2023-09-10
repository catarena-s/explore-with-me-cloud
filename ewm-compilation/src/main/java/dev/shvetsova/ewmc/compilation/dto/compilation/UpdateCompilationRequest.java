
package dev.shvetsova.ewmc.compilation.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Изменение информации о подборке событий. Если поле в запросе не указано (равно null) - значит изменение этих данных не треубется.
 */
@Data
@Builder(toBuilder = true)
public class UpdateCompilationRequest {
    @Size(min = 1, max = 50, message = "size must be between 1 and 50")
    private String title;
    private Boolean pinned;
    private List<Long> events;
}
