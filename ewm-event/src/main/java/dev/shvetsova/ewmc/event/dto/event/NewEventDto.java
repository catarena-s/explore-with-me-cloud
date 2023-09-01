package dev.shvetsova.ewmc.event.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.shvetsova.ewmc.event.dto.location.LocationDto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static dev.shvetsova.ewmc.event.utils.Constants.YYYY_MM_DD_HH_MM_SS;

/**
 * Новое событие
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank(message = "User name cannot be empty or null")
    @Size(min = 3, max = 120, message = "size must be between 3 and 120")
    private String title;

    @NotBlank(message = "User name cannot be empty or null")
    @Size(min = 20, max = 2000, message = "size must be between 20 and 2000")
    private String annotation;

    @NotBlank(message = "User name cannot be empty or null")
    @Size(min = 20, max = 7000, message = "size must be between 20 and 7000")
    private String description;

    @NotNull(message = "Category cannot be empty or null")
    @Positive(message = "CategoryId must be positive")
    private Long category;

    @NotNull(message = "EventDate cannot be empty or null")
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime eventDate;

    @NotNull(message = "Location cannot be empty or null")
    private LocationDto location;

    //Нужно ли оплачивать участие в событии
    @NotNull(message = "Paid cannot be empty or null")
    private boolean paid = false;

    //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    @NotNull(message = "Participant limit cannot be empty or null")
    @PositiveOrZero(message = "Participant limit must be positive or zero")
    private Integer participantLimit = 0;
    /*
    Нужна ли пре-модерация заявок на участие.
    Если true, то все заявки будут ожидать подтверждения инициатором события.
    Если false - то будут подтверждаться автоматически.
     */
    @NotNull(message = "Request moderation cannot be empty or null")
    private boolean requestModeration = true;

}
