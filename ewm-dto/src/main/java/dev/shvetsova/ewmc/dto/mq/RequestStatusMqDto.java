package dev.shvetsova.ewmc.dto.mq;

import java.util.List;

public class RequestStatusMqDto {
    private List<Long> request;
    private long userId;
    private long eventId;
    private String newStatus;

    public RequestStatusMqDto() {
    }

    public RequestStatusMqDto(List<Long> request, long userId, long eventId, String newStatus) {
        this.request = request;
        this.userId = userId;
        this.eventId = eventId;
        this.newStatus = newStatus;
    }

    public static RequestMqDtoBuilder builder() {
        return new RequestMqDtoBuilder();
    }

    public List<Long> getRequest() {
        return this.request;
    }

    public long getUserId() {
        return this.userId;
    }

    public long getEventId() {
        return this.eventId;
    }

    public String getNewStatus() {
        return this.newStatus;
    }

    public static class RequestMqDtoBuilder {
        private List<Long> request;
        private long userId;
        private long eventId;
        private String newStatus;

        RequestMqDtoBuilder() {
        }

        public RequestMqDtoBuilder request(List<Long> request) {
            this.request = request;
            return this;
        }

        public RequestMqDtoBuilder userId(long userId) {
            this.userId = userId;
            return this;
        }

        public RequestMqDtoBuilder eventId(long eventId) {
            this.eventId = eventId;
            return this;
        }

        public RequestMqDtoBuilder newStatus(String newStatus) {
            this.newStatus = newStatus;
            return this;
        }

        public RequestStatusMqDto build() {
            return new RequestStatusMqDto(this.request, this.userId, this.eventId, this.newStatus);
        }
    }
}
