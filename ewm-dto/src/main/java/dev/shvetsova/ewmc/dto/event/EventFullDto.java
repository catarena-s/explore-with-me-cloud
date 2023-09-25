package dev.shvetsova.ewmc.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.shvetsova.ewmc.dto.category.CategoryDto;
import dev.shvetsova.ewmc.dto.location.LocationDto;
import dev.shvetsova.ewmc.dto.user.UserShortDto;
import dev.shvetsova.ewmc.utils.Constants;

import java.time.LocalDateTime;


public class EventFullDto {
    private Long id;
    private String title;// Заголовок
    private String annotation;// Краткое описание
    private CategoryDto category;
    private Boolean paid;
    // Дата и время на которые намечено событие (в формате yyyy-MM-dd HH:mm:ss)
    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    // Количество одобренных заявок на участие в данном событии
    private Integer confirmedRequests;
    private String description;// Полное описание события
    // Нужно ли оплачивать участие
    // Ограничение на количество участников.
    // Значение 0 - означает отсутствие ограничения
    private Integer participantLimit;
    private String state;// Список состояний жизненного цикла события
    // Дата и время создания события
    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime createdOn;
    // Дата и время публикации события (в формате &quot;yyyy-MM-dd HH:mm:ss
    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
    private LocalDateTime publishedOn;

    private LocationDto location;
    // Нужна ли пре-модерация заявок на участие
    private Boolean requestModeration;
    private Long views = 0L;// Количество просмотрев события

    public EventFullDto(Long id, String title, String annotation, CategoryDto category, Boolean paid, LocalDateTime eventDate, UserShortDto initiator, Integer confirmedRequests, String description, Integer participantLimit, String state, LocalDateTime createdOn, LocalDateTime publishedOn, LocationDto location, Boolean requestModeration, Long views) {
        this.id = id;
        this.title = title;
        this.annotation = annotation;
        this.category = category;
        this.paid = paid;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.confirmedRequests = confirmedRequests;
        this.description = description;
        this.participantLimit = participantLimit;
        this.state = state;
        this.createdOn = createdOn;
        this.publishedOn = publishedOn;
        this.location = location;
        this.requestModeration = requestModeration;
        this.views = views;
    }

    public EventFullDto() {
    }

    public static EventFullDtoBuilder builder() {
        return new EventFullDtoBuilder();
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

    public Boolean getPaid() {
        return this.paid;
    }

    public LocalDateTime getEventDate() {
        return this.eventDate;
    }

    public UserShortDto getInitiator() {
        return this.initiator;
    }

    public Integer getConfirmedRequests() {
        return this.confirmedRequests;
    }

    public String getDescription() {
        return this.description;
    }

    public Integer getParticipantLimit() {
        return this.participantLimit;
    }

    public String getState() {
        return this.state;
    }

    public LocalDateTime getCreatedOn() {
        return this.createdOn;
    }

    public LocalDateTime getPublishedOn() {
        return this.publishedOn;
    }

    public LocationDto getLocation() {
        return this.location;
    }

    public Boolean getRequestModeration() {
        return this.requestModeration;
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

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public void setInitiator(UserShortDto initiator) {
        this.initiator = initiator;
    }

    public void setConfirmedRequests(Integer confirmedRequests) {
        this.confirmedRequests = confirmedRequests;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParticipantLimit(Integer participantLimit) {
        this.participantLimit = participantLimit;
    }

    public void setState(String state) {
        this.state = state;
    }

    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
    public void setPublishedOn(LocalDateTime publishedOn) {
        this.publishedOn = publishedOn;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public void setRequestModeration(Boolean requestModeration) {
        this.requestModeration = requestModeration;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public EventFullDtoBuilder toBuilder() {
        return new EventFullDtoBuilder().id(this.id).title(this.title).annotation(this.annotation).category(this.category).paid(this.paid).eventDate(this.eventDate).initiator(this.initiator).confirmedRequests(this.confirmedRequests).description(this.description).participantLimit(this.participantLimit).state(this.state).createdOn(this.createdOn).publishedOn(this.publishedOn).location(this.location).requestModeration(this.requestModeration).views(this.views);
    }

    public static class EventFullDtoBuilder {
        private Long id;
        private String title;
        private String annotation;
        private CategoryDto category;
        private Boolean paid;
        private LocalDateTime eventDate;
        private UserShortDto initiator;
        private Integer confirmedRequests;
        private String description;
        private Integer participantLimit;
        private String state;
        private LocalDateTime createdOn;
        private LocalDateTime publishedOn;
        private LocationDto location;
        private Boolean requestModeration;
        private Long views;

        EventFullDtoBuilder() {
        }

        public EventFullDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public EventFullDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public EventFullDtoBuilder annotation(String annotation) {
            this.annotation = annotation;
            return this;
        }

        public EventFullDtoBuilder category(CategoryDto category) {
            this.category = category;
            return this;
        }

        public EventFullDtoBuilder paid(Boolean paid) {
            this.paid = paid;
            return this;
        }

        @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
        public EventFullDtoBuilder eventDate(LocalDateTime eventDate) {
            this.eventDate = eventDate;
            return this;
        }

        public EventFullDtoBuilder initiator(UserShortDto initiator) {
            this.initiator = initiator;
            return this;
        }

        public EventFullDtoBuilder confirmedRequests(Integer confirmedRequests) {
            this.confirmedRequests = confirmedRequests;
            return this;
        }

        public EventFullDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EventFullDtoBuilder participantLimit(Integer participantLimit) {
            this.participantLimit = participantLimit;
            return this;
        }

        public EventFullDtoBuilder state(String state) {
            this.state = state;
            return this;
        }

        @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
        public EventFullDtoBuilder createdOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        @JsonFormat(pattern = Constants.YYYY_MM_DD_HH_MM_SS)
        public EventFullDtoBuilder publishedOn(LocalDateTime publishedOn) {
            this.publishedOn = publishedOn;
            return this;
        }

        public EventFullDtoBuilder location(LocationDto location) {
            this.location = location;
            return this;
        }

        public EventFullDtoBuilder requestModeration(Boolean requestModeration) {
            this.requestModeration = requestModeration;
            return this;
        }

        public EventFullDtoBuilder views(Long views) {
            this.views = views;
            return this;
        }

        public EventFullDto build() {
            return new EventFullDto(this.id, this.title, this.annotation, this.category, this.paid, this.eventDate, this.initiator, this.confirmedRequests, this.description, this.participantLimit, this.state, this.createdOn, this.publishedOn, this.location, this.requestModeration, this.views);
        }
    }
}
