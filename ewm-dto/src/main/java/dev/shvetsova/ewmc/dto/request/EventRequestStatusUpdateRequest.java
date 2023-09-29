package dev.shvetsova.ewmc.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class EventRequestStatusUpdateRequest {
    @NotNull(message = "RequestId list cannot be null.")
    @NotEmpty(message = "RequestId list cannot be empty.")
    private List<Long> requestIds;
    @NotNull(message = "Request status cannot be null or empty.")
    private String status;

    public EventRequestStatusUpdateRequest(@NotNull(message = "RequestId list cannot be null.") @NotEmpty(message = "RequestId list cannot be empty.") List<Long> requestIds, @NotNull(message = "Request status cannot be null or empty.") String status) {
        this.requestIds = requestIds;
        this.status = status;
    }

    public static EventRequestStatusUpdateRequestBuilder builder() {
        return new EventRequestStatusUpdateRequestBuilder();
    }

    public @NotNull(message = "RequestId list cannot be null.") @NotEmpty(message = "RequestId list cannot be empty.") List<Long> getRequestIds() {
        return this.requestIds;
    }

    public @NotNull(message = "Request status cannot be null or empty.") String getStatus() {
        return this.status;
    }

    public static class EventRequestStatusUpdateRequestBuilder {
        private @NotNull(message = "RequestId list cannot be null.") @NotEmpty(message = "RequestId list cannot be empty.") List<Long> requestIds;
        private @NotNull(message = "Request status cannot be null or empty.") String status;

        EventRequestStatusUpdateRequestBuilder() {
        }

        public EventRequestStatusUpdateRequestBuilder requestIds(@NotNull(message = "RequestId list cannot be null.") @NotEmpty(message = "RequestId list cannot be empty.") List<Long> requestIds) {
            this.requestIds = requestIds;
            return this;
        }

        public EventRequestStatusUpdateRequestBuilder status(@NotNull(message = "Request status cannot be null or empty.") String status) {
            this.status = status;
            return this;
        }

        public EventRequestStatusUpdateRequest build() {
            return new EventRequestStatusUpdateRequest(this.requestIds, this.status);
        }
    }
}
