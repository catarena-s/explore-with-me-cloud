package dev.shvetsova.ewmc.common.dto.request;

import dev.shvetsova.ewmc.common.enums.RequestStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    @NotNull(message = "RequestId list cannot be null.")
    @NotEmpty(message = "RequestId list cannot be empty.")
    private List<Long> requestIds;
    @NotNull(message = "Request status cannot be null or empty.")
    private RequestStatus status;
}
