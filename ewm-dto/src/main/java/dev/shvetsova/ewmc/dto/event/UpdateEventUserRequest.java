package dev.shvetsova.ewmc.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.shvetsova.ewmc.dto.location.LocationDto;
import dev.shvetsova.ewmc.utils.Constants;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Данные для изменения информации о событии. Если поле в запросе не указано (равно null) - значит изменение этих данных не треубется.
 */

public class UpdateEventUserRequest {
    @Size(min = 3, max = 120, message = "size must be between 3 and 20")
    private String title;
    @Size(min = 20, max = 2000, message = "size must be between 20 and 2000")
    private String annotation;
    @Size(min = 20, max = 7000, message = "size must be between 20 and 7000")
    private String description;
    private Long category;
    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;

    public UpdateEventUserRequest(@Size(min = 3, max = 120, message = "size must be between 3 and 20") String title, @Size(min = 20, max = 2000, message = "size must be between 20 and 2000") String annotation, @Size(min = 20, max = 7000, message = "size must be between 20 and 7000") String description, Long category, LocalDateTime eventDate, LocationDto location, Boolean paid, Integer participantLimit, Boolean requestModeration, String stateAction) {
        this.title = title;
        this.annotation = annotation;
        this.description = description;
        this.category = category;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.stateAction = stateAction;
    }

    public UpdateEventUserRequest() {
    }

    public static UpdateEventUserRequestBuilder builder() {
        return new UpdateEventUserRequestBuilder();
    }

    public @Size(min = 3, max = 120, message = "size must be between 3 and 20") String getTitle() {
        return this.title;
    }

    public @Size(min = 20, max = 2000, message = "size must be between 20 and 2000") String getAnnotation() {
        return this.annotation;
    }

    public @Size(min = 20, max = 7000, message = "size must be between 20 and 7000") String getDescription() {
        return this.description;
    }

    public Long getCategory() {
        return this.category;
    }

    public LocalDateTime getEventDate() {
        return this.eventDate;
    }

    public LocationDto getLocation() {
        return this.location;
    }

    public Boolean getPaid() {
        return this.paid;
    }

    public Integer getParticipantLimit() {
        return this.participantLimit;
    }

    public Boolean getRequestModeration() {
        return this.requestModeration;
    }

    public String getStateAction() {
        return this.stateAction;
    }

    public void setTitle(@Size(min = 3, max = 120, message = "size must be between 3 and 20") String title) {
        this.title = title;
    }

    public void setAnnotation(@Size(min = 20, max = 2000, message = "size must be between 20 and 2000") String annotation) {
        this.annotation = annotation;
    }

    public void setDescription(@Size(min = 20, max = 7000, message = "size must be between 20 and 7000") String description) {
        this.description = description;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public void setParticipantLimit(Integer participantLimit) {
        this.participantLimit = participantLimit;
    }

    public void setRequestModeration(Boolean requestModeration) {
        this.requestModeration = requestModeration;
    }

    public void setStateAction(String stateAction) {
        this.stateAction = stateAction;
    }

    public String toString() {
        return "UpdateEventUserRequest(title=" + this.getTitle() + ", annotation=" + this.getAnnotation() + ", description=" + this.getDescription() + ", category=" + this.getCategory() + ", eventDate=" + this.getEventDate() + ", location=" + this.getLocation() + ", paid=" + this.getPaid() + ", participantLimit=" + this.getParticipantLimit() + ", requestModeration=" + this.getRequestModeration() + ", stateAction=" + this.getStateAction() + ")";
    }

    public static class UpdateEventUserRequestBuilder {
        private @Size(min = 3, max = 120, message = "size must be between 3 and 20") String title;
        private @Size(min = 20, max = 2000, message = "size must be between 20 and 2000") String annotation;
        private @Size(min = 20, max = 7000, message = "size must be between 20 and 7000") String description;
        private Long category;
        private LocalDateTime eventDate;
        private LocationDto location;
        private Boolean paid;
        private Integer participantLimit;
        private Boolean requestModeration;
        private String stateAction;

        UpdateEventUserRequestBuilder() {
        }

        public UpdateEventUserRequestBuilder title(@Size(min = 3, max = 120, message = "size must be between 3 and 20") String title) {
            this.title = title;
            return this;
        }

        public UpdateEventUserRequestBuilder annotation(@Size(min = 20, max = 2000, message = "size must be between 20 and 2000") String annotation) {
            this.annotation = annotation;
            return this;
        }

        public UpdateEventUserRequestBuilder description(@Size(min = 20, max = 7000, message = "size must be between 20 and 7000") String description) {
            this.description = description;
            return this;
        }

        public UpdateEventUserRequestBuilder category(Long category) {
            this.category = category;
            return this;
        }

        @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
        public UpdateEventUserRequestBuilder eventDate(LocalDateTime eventDate) {
            this.eventDate = eventDate;
            return this;
        }

        public UpdateEventUserRequestBuilder location(LocationDto location) {
            this.location = location;
            return this;
        }

        public UpdateEventUserRequestBuilder paid(Boolean paid) {
            this.paid = paid;
            return this;
        }

        public UpdateEventUserRequestBuilder participantLimit(Integer participantLimit) {
            this.participantLimit = participantLimit;
            return this;
        }

        public UpdateEventUserRequestBuilder requestModeration(Boolean requestModeration) {
            this.requestModeration = requestModeration;
            return this;
        }

        public UpdateEventUserRequestBuilder stateAction(String stateAction) {
            this.stateAction = stateAction;
            return this;
        }

        public UpdateEventUserRequest build() {
            return new UpdateEventUserRequest(this.title, this.annotation, this.description, this.category, this.eventDate, this.location, this.paid, this.participantLimit, this.requestModeration, this.stateAction);
        }

        public String toString() {
            return "UpdateEventUserRequest.UpdateEventUserRequestBuilder(title=" + this.title + ", annotation=" + this.annotation + ", description=" + this.description + ", category=" + this.category + ", eventDate=" + this.eventDate + ", location=" + this.location + ", paid=" + this.paid + ", participantLimit=" + this.participantLimit + ", requestModeration=" + this.requestModeration + ", stateAction=" + this.stateAction + ")";
        }
    }
}