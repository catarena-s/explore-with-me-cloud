package dev.shvetsova.ewmc.main.mapper;

import dev.shvetsova.ewmc.common.dto.request.ParticipationRequestDto;
import dev.shvetsova.ewmc.main.model.Request;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static dev.shvetsova.ewmc.common.Constants.FORMATTER;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    public static ParticipationRequestDto toDto(Request newRequest) {
        return ParticipationRequestDto.builder()
                .id(newRequest.getId())
                .requester(newRequest.getRequesterId())
                .event(newRequest.getEvent().getId())
                .status(newRequest.getStatus())
                .created(newRequest.getCreated().format(FORMATTER))
                .isPrivate(newRequest.isPrivate())
                .build();
    }
}
