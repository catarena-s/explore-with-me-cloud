package dev.shvetsova.ewmc.dto.request;

/**
 * Заявка на участие в событии
 */
public class ParticipationRequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private String created;
    private String status;
    private boolean isPrivate;

    public ParticipationRequestDto(Long id, Long event, Long requester, String created, String status, boolean isPrivate) {
        this.id = id;
        this.event = event;
        this.requester = requester;
        this.created = created;
        this.status = status;
        this.isPrivate = isPrivate;
    }

    public ParticipationRequestDto() {
    }

    public static ParticipationRequestDtoBuilder builder() {
        return new ParticipationRequestDtoBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public Long getEvent() {
        return this.event;
    }

    public Long getRequester() {
        return this.requester;
    }

    public String getCreated() {
        return this.created;
    }

    public String getStatus() {
        return this.status;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEvent(Long event) {
        this.event = event;
    }

    public void setRequester(Long requester) {
        this.requester = requester;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public static class ParticipationRequestDtoBuilder {
        private Long id;
        private Long event;
        private Long requester;
        private String created;
        private String status;
        private boolean isPrivate;

        ParticipationRequestDtoBuilder() {
        }

        public ParticipationRequestDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ParticipationRequestDtoBuilder event(Long event) {
            this.event = event;
            return this;
        }

        public ParticipationRequestDtoBuilder requester(Long requester) {
            this.requester = requester;
            return this;
        }

        public ParticipationRequestDtoBuilder created(String created) {
            this.created = created;
            return this;
        }

        public ParticipationRequestDtoBuilder status(String status) {
            this.status = status;
            return this;
        }

        public ParticipationRequestDtoBuilder isPrivate(boolean isPrivate) {
            this.isPrivate = isPrivate;
            return this;
        }

        public ParticipationRequestDto build() {
            return new ParticipationRequestDto(this.id, this.event, this.requester, this.created, this.status, this.isPrivate);
        }
    }
}
