package dev.shvetsova.ewmc.request.mapper;

import dev.shvetsova.ewmc.dto.request.ParticipationRequestDto;
import dev.shvetsova.ewmc.request.model.Request;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static dev.shvetsova.ewmc.utils.Constants.FORMATTER;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    public static ParticipationRequestDto toDto(Request newRequest) {
        return ParticipationRequestDto.builder()
                .id(newRequest.getId())
                .requester(newRequest.getRequesterId())
                .event(newRequest.getEventId())
                .status(newRequest.getStatus().name())
                .created(newRequest.getCreated().format(FORMATTER))
                .isPrivate(newRequest.isPrivate())
                .build();
    }
}
