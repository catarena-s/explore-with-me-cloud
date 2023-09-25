package dev.shvetsova.ewmc.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.shvetsova.ewmc.dto.category.CategoryDto;
import dev.shvetsova.ewmc.dto.user.UserShortDto;

import java.time.LocalDateTime;

import static dev.shvetsova.ewmc.utils.Constants.YYYY_MM_DD_HH_MM_SS;

/**
 * Краткая информация о событии
 */
public class EventShortDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private Long views = 0L;

    public EventShortDto(Long id, String title, String annotation, CategoryDto category, Integer confirmedRequests, LocalDateTime eventDate, UserShortDto initiator, Boolean paid, Long views) {
        this.id = id;
        this.title = title;
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.paid = paid;
        this.views = views;
    }

    public EventShortDto() {
    }

    public static EventShortDtoBuilder builder() {
        return new EventShortDtoBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAnnotation() {
        return this.annotation;
    }

    public CategoryDto getCategory() {
        return this.category;
    }

    public Integer getConfirmedRequests() {
        return this.confirmedRequests;
    }

    public LocalDateTime getEventDate() {
        return this.eventDate;
    }

    public UserShortDto getInitiator() {
        return this.initiator;
    }

    public Boolean getPaid() {
        return this.paid;
    }

    public Long getViews() {
        return this.views;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public void setConfirmedRequests(Integer confirmedRequests) {
        this.confirmedRequests = confirmedRequests;
    }

    @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS)
    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public void setInitiator(UserShortDto initiator) {
        this.initiator = initiator;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public EventShortDtoBuilder toBuilder() {
        return new EventShortDtoBuilder().id(this.id).title(this.title).annotation(this.annotation).category(this.category).confirmedRequests(this.confirmedRequests).eventDate(this.eventDate).initiator(this.initiator).paid(this.paid).views(this.views);
    }

    public static class EventShortDtoBuilder {
        private Long id;
        private String title;
        private String annotation;
        private CategoryDto category;
        private Integer confirmedRequests;
        private LocalDateTime eventDate;
        private UserShortDto initiator;
        private Boolean paid;
        private Long views;

        EventShortDtoBuilder() {
        }

        public EventShortDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EventShortDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public EventShortDtoBuilder annotation(String annotation) {
            this.annotation = annotation;
            return this;
        }

        public EventShortDtoBuilder category(CategoryDto category) {
            this.category = category;
            return this;
        }

        public EventShortDtoBuilder confirmedRequests(Integer confirmedRequests) {
            this.confirmedRequests = confirmedRequests;
            return this;
        }

        @JsonFormat(pattern = YYYY_MM_DD_HH_MM_SS)
        public EventShortDtoBuilder eventDate(LocalDateTime eventDate) {
            this.eventDate = eventDate;
            return this;
        }

        public EventShortDtoBuilder initiator(UserShortDto initiator) {
            this.initiator = initiator;
            return this;
        }

        public EventShortDtoBuilder paid(Boolean paid) {
            this.paid = paid;
            return this;
        }

        public EventShortDtoBuilder views(Long views) {
            this.views = views;
            return this;
        }

        public EventShortDto build() {
            return new EventShortDto(this.id, this.title, this.annotation, this.category, this.confirmedRequests, this.eventDate, this.initiator, this.paid, this.views);
        }
    }
}
