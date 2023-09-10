package dev.shvetsova.ewmc.compilation.dto.compilation;

import dev.shvetsova.ewmc.compilation.dto.event.EventShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Подборка событий
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Long id;
    private Boolean pinned;
    private String title;
    private List<EventShortDto> events;
}
