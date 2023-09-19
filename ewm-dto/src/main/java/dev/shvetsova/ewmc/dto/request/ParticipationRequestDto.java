package dev.shvetsova.ewmc.dto.request;

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
    private String  requester;
    private String created;
    private String status;
    private boolean isPrivate;
}
