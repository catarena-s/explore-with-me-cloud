package dev.shvetsova.ewmc.dto.mq;

public class RequestMqDto {
    private Long requestId;
    private Long eventId;
    private String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RequestMqDto() {
    }

    public RequestMqDto(Long requestId, Long eventId, String userId) {
        this.requestId = requestId;
        this.eventId = eventId;
        this.userId = userId;
    }
}
