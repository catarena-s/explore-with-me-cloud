package dev.shvetsova.ewmc.dto.mq;

public class RequestMqDto {
    private Long requestId;
    private Long eventId;
    private Long userId;

    public RequestMqDto() {
    }

    public RequestMqDto(Long requestId, Long eventId, Long userId) {
        this.requestId = requestId;
        this.eventId = eventId;
        this.userId = userId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
