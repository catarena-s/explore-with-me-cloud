package dev.shvetsova.ewmc.common.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.shvetsova.ewmc.common.dto.location.LocationDto;
import dev.shvetsova.ewmc.common.enums.EventStateAction;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import static dev.shvetsova.ewmc.common.Constants.YYYY_MM_DD_HH_MM_SS;

/**
 * Данные для изменения информации о событии. Если поле в запросе не указано (равно null) - значит изменение этих данных не треубется.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventAdminRequest {
    @Size(min = 3, max = 120, message = "size must be between 3 and 20")
    private String title;
    @Size(min = 20, max = 2000, message = "size must be between 20 and 2000")
    private String annotation;
    @Size(min = 20, max = 7000, message = "size must be between 20 and 7000")
    private String description;
    private Long category;
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
}

