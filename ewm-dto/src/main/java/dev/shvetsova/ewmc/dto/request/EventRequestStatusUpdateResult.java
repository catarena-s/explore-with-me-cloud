package dev.shvetsova.ewmc.dto.request;

import java.util.List;

public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;

    public EventRequestStatusUpdateResult(List<ParticipationRequestDto> confirmedRequests, List<ParticipationRequestDto> rejectedRequests) {
        this.confirmedRequests = confirmedRequests;
        this.rejectedRequests = rejectedRequests;
    }

    public EventRequestStatusUpdateResult() {
    }

    public static EventRequestStatusUpdateResultBuilder builder() {
        return new EventRequestStatusUpdateResultBuilder();
    }

    public List<ParticipationRequestDto> getConfirmedRequests() {
        return this.confirmedRequests;
    }

    public List<ParticipationRequestDto> getRejectedRequests() {
        return this.rejectedRequests;
    }

    public void setConfirmedRequests(List<ParticipationRequestDto> confirmedRequests) {
        this.confirmedRequests = confirmedRequests;
    }

    public void setRejectedRequests(List<ParticipationRequestDto> rejectedRequests) {
        this.rejectedRequests = rejectedRequests;
    }

    public static class EventRequestStatusUpdateResultBuilder {
        private List<ParticipationRequestDto> confirmedRequests;
        private List<ParticipationRequestDto> rejectedRequests;

        EventRequestStatusUpdateResultBuilder() {
        }

        public EventRequestStatusUpdateResultBuilder confirmedRequests(List<ParticipationRequestDto> confirmedRequests) {
            this.confirmedRequests = confirmedRequests;
            return this;
        }

        public EventRequestStatusUpdateResultBuilder rejectedRequests(List<ParticipationRequestDto> rejectedRequests) {
            this.rejectedRequests = rejectedRequests;
            return this;
        }

        public EventRequestStatusUpdateResult build() {
            return new EventRequestStatusUpdateResult(this.confirmedRequests, this.rejectedRequests);
        }
    }
}
