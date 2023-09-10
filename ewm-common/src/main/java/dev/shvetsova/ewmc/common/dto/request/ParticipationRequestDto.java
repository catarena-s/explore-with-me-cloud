package dev.shvetsova.ewmc.common.dto.request;

import dev.shvetsova.ewmc.common.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Заявка на участие в событии
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private String created;
    private RequestStatus status;
    private boolean isPrivate;
}