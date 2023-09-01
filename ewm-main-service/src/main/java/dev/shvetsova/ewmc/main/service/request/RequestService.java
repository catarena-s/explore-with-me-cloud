package dev.shvetsova.ewmc.main.service.request;

import dev.shvetsova.ewmc.common.dto.request.EventRequestStatusUpdateResult;
import dev.shvetsova.ewmc.common.dto.request.EventRequestStatusUpdateRequest;
import dev.shvetsova.ewmc.common.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto addParticipationRequest(long userId, long eventId);

    List<ParticipationRequestDto> getEventParticipants(long userId, long eventId);

    EventRequestStatusUpdateResult changeRequestStatus(EventRequestStatusUpdateRequest body, long userId, long eventId);

    List<ParticipationRequestDto> getUserRequests(long userId);

    ParticipationRequestDto cancelRequest(long userId, long requestId);

    List<ParticipationRequestDto> changeVisibilityEventParticipation(long userId, List<Long> ids, boolean hide);

    boolean isExistByRequester(long userId);
}
