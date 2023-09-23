package dev.shvetsova.ewmc.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.shvetsova.ewmc.dto.location.LocationDto;
import dev.shvetsova.ewmc.utils.Constants;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;


/**
 * Новое событие
 */
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
    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
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

    public NewEventDto(@NotBlank(message = "User name cannot be empty or null") @Size(min = 3, max = 120, message = "size must be between 3 and 120") String title, @NotBlank(message = "User name cannot be empty or null") @Size(min = 20, max = 2000, message = "size must be between 20 and 2000") String annotation, @NotBlank(message = "User name cannot be empty or null") @Size(min = 20, max = 7000, message = "size must be between 20 and 7000") String description, @NotNull(message = "Category cannot be empty or null") @Positive(message = "CategoryId must be positive") Long category, @NotNull(message = "EventDate cannot be empty or null") LocalDateTime eventDate, @NotNull(message = "Location cannot be empty or null") LocationDto location, @NotNull(message = "Paid cannot be empty or null") boolean paid, @NotNull(message = "Participant limit cannot be empty or null") @PositiveOrZero(message = "Participant limit must be positive or zero") Integer participantLimit, @NotNull(message = "Request moderation cannot be empty or null") boolean requestModeration) {
        this.title = title;
        this.annotation = annotation;
        this.description = description;
        this.category = category;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
    }

    public NewEventDto() {
    }

    public static NewEventDtoBuilder builder() {
        return new NewEventDtoBuilder();
    }

    public NewEventDtoBuilder toBuilder() {
        return new NewEventDtoBuilder().title(this.title).annotation(this.annotation).description(this.description).category(this.category).eventDate(this.eventDate).location(this.location).paid(this.paid).participantLimit(this.participantLimit).requestModeration(this.requestModeration);
    }

    public String getTitle() {
        return title;
    }

    public String getAnnotation() {
        return annotation;
    }

    public String getDescription() {
        return description;
    }

    public Long getCategory() {
        return category;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public LocationDto getLocation() {
        return location;
    }

    public boolean isPaid() {
        return paid;
    }

    public Integer getParticipantLimit() {
        return participantLimit;
    }

    public boolean isRequestModeration() {
        return requestModeration;
    }

    public static class NewEventDtoBuilder {
        private @NotBlank(message = "User name cannot be empty or null") @Size(min = 3, max = 120, message = "size must be between 3 and 120") String title;
        private @NotBlank(message = "User name cannot be empty or null") @Size(min = 20, max = 2000, message = "size must be between 20 and 2000") String annotation;
        private @NotBlank(message = "User name cannot be empty or null") @Size(min = 20, max = 7000, message = "size must be between 20 and 7000") String description;
        private @NotNull(message = "Category cannot be empty or null") @Positive(message = "CategoryId must be positive") Long category;
        private @NotNull(message = "EventDate cannot be empty or null") LocalDateTime eventDate;
        private @NotNull(message = "Location cannot be empty or null") LocationDto location;
        private @NotNull(message = "Paid cannot be empty or null") boolean paid;
        private @NotNull(message = "Participant limit cannot be empty or null") @PositiveOrZero(message = "Participant limit must be positive or zero") Integer participantLimit;
        private @NotNull(message = "Request moderation cannot be empty or null") boolean requestModeration;

        NewEventDtoBuilder() {
        }

        public NewEventDtoBuilder title(@NotBlank(message = "User name cannot be empty or null") @Size(min = 3, max = 120, message = "size must be between 3 and 120") String title) {
            this.title = title;
            return this;
        }

        public NewEventDtoBuilder annotation(@NotBlank(message = "User name cannot be empty or null") @Size(min = 20, max = 2000, message = "size must be between 20 and 2000") String annotation) {
            this.annotation = annotation;
            return this;
        }

        public NewEventDtoBuilder description(@NotBlank(message = "User name cannot be empty or null") @Size(min = 20, max = 7000, message = "size must be between 20 and 7000") String description) {
            this.description = description;
            return this;
        }

        public NewEventDtoBuilder category(@NotNull(message = "Category cannot be empty or null") @Positive(message = "CategoryId must be positive") Long category) {
            this.category = category;
            return this;
        }

        @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
        public NewEventDtoBuilder eventDate(@NotNull(message = "EventDate cannot be empty or null") LocalDateTime eventDate) {
            this.eventDate = eventDate;
            return this;
        }

        public NewEventDtoBuilder location(@NotNull(message = "Location cannot be empty or null") LocationDto location) {
            this.location = location;
            return this;
        }

        public NewEventDtoBuilder paid(@NotNull(message = "Paid cannot be empty or null") boolean paid) {
            this.paid = paid;
            return this;
        }

        public NewEventDtoBuilder participantLimit(@NotNull(message = "Participant limit cannot be empty or null") @PositiveOrZero(message = "Participant limit must be positive or zero") Integer participantLimit) {
            this.participantLimit = participantLimit;
            return this;
        }

        public NewEventDtoBuilder requestModeration(@NotNull(message = "Request moderation cannot be empty or null") boolean requestModeration) {
            this.requestModeration = requestModeration;
            return this;
        }

        public NewEventDto build() {
            return new NewEventDto(this.title, this.annotation, this.description, this.category, this.eventDate, this.location, this.paid, this.participantLimit, this.requestModeration);
        }

        public String toString() {
            return "NewEventDto.NewEventDtoBuilder(title=" + this.title + ", annotation=" + this.annotation + ", description=" + this.description + ", category=" + this.category + ", eventDate=" + this.eventDate + ", location=" + this.location + ", paid=" + this.paid + ", participantLimit=" + this.participantLimit + ", requestModeration=" + this.requestModeration + ")";
        }
    }
}
