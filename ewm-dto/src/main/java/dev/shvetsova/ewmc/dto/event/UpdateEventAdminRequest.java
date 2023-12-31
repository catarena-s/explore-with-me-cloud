package dev.shvetsova.ewmc.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.shvetsova.ewmc.dto.location.LocationDto;
import dev.shvetsova.ewmc.utils.Constants;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;


/**
 * Данные для изменения информации о событии. Если поле в запросе не указано (равно null) - значит изменение этих данных не треубется.
 */
public class UpdateEventAdminRequest {
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

    public UpdateEventAdminRequest(@Size(min = 3, max = 120, message = "size must be between 3 and 20") String title,
                                   @Size(min = 20, max = 2000, message = "size must be between 20 and 2000") String annotation,
                                   @Size(min = 20, max = 7000, message = "size must be between 20 and 7000") String description,
                                   Long category, LocalDateTime eventDate, LocationDto location, Boolean paid, Integer participantLimit,
                                   Boolean requestModeration, String stateAction) {
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

    public UpdateEventAdminRequest() {
    }

    public static UpdateEventAdminRequestBuilder builder() {
        return new UpdateEventAdminRequestBuilder();
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

    public Boolean getPaid() {
        return paid;
    }

    public Integer getParticipantLimit() {
        return participantLimit;
    }

    public Boolean getRequestModeration() {
        return requestModeration;
    }

    public String getStateAction() {
        return stateAction;
    }

    public static class UpdateEventAdminRequestBuilder {
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

        UpdateEventAdminRequestBuilder() {
        }

        public UpdateEventAdminRequestBuilder title(@Size(min = 3, max = 120, message = "size must be between 3 and 20") String title) {
            this.title = title;
            return this;
        }

        public UpdateEventAdminRequestBuilder annotation(@Size(min = 20, max = 2000, message = "size must be between 20 and 2000") String annotation) {
            this.annotation = annotation;
            return this;
        }

        public UpdateEventAdminRequestBuilder description(@Size(min = 20, max = 7000, message = "size must be between 20 and 7000") String description) {
            this.description = description;
            return this;
        }

        public UpdateEventAdminRequestBuilder category(Long category) {
            this.category = category;
            return this;
        }

        @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
        public UpdateEventAdminRequestBuilder eventDate(LocalDateTime eventDate) {
            this.eventDate = eventDate;
            return this;
        }

        public UpdateEventAdminRequestBuilder location(LocationDto location) {
            this.location = location;
            return this;
        }

        public UpdateEventAdminRequestBuilder paid(Boolean paid) {
            this.paid = paid;
            return this;
        }

        public UpdateEventAdminRequestBuilder participantLimit(Integer participantLimit) {
            this.participantLimit = participantLimit;
            return this;
        }

        public UpdateEventAdminRequestBuilder requestModeration(Boolean requestModeration) {
            this.requestModeration = requestModeration;
            return this;
        }

        public UpdateEventAdminRequestBuilder stateAction(String stateAction) {
            this.stateAction = stateAction;
            return this;
        }

        public UpdateEventAdminRequest build() {
            return new UpdateEventAdminRequest(this.title, this.annotation, this.description, this.category, this.eventDate, this.location, this.paid, this.participantLimit, this.requestModeration, this.stateAction);
        }
    }
}

