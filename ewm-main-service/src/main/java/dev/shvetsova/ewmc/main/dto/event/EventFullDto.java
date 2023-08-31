package dev.shvetsova.ewmc.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.shvetsova.ewmc.main.dto.category.CategoryDto;
import dev.shvetsova.ewmc.main.enums.EventState;
import dev.shvetsova.ewmc.main.dto.location.LocationDto;
import dev.shvetsova.ewmc.main.dto.user.UserShortDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import static dev.shvetsova.ewmc.common.Constants.YYYY_MM_DD_HH_MM_SS;

//import static dev.shvetsova.ewmc.common.Constants.YYYY_MM_DD_HH_MM_SS;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private String title;// Заголовок
    private String annotation;// Краткое описание
    private CategoryDto category;
    private Boolean paid;
    // Дата и время на которые намечено событие (в формате yyyy-MM-dd HH:mm:ss)
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    // Количество одобренных заявок на участие в данном событии
    private Integer confirmedRequests;
    private String description;// Полное описание события
    // Нужно ли оплачивать участие
    // Ограничение на количество участников.
    // Значение 0 - означает отсутствие ограничения
    private Integer participantLimit;
    private EventState state;// Список состояний жизненного цикла события
    // Дата и время создания события
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createdOn;
    // Дата и время публикации события (в формате &quot;yyyy-MM-dd HH:mm:ss
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime publishedOn;

    private LocationDto location;
    // Нужна ли пре-модерация заявок на участие
    private Boolean requestModeration;
    private Long views = 0L;// Количество просмотрев события
}
