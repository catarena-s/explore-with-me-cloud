package dev.shvetsova.ewmc.request.service.request;

import dev.shvetsova.ewmc.dto.request.ParticipationRequestDto;
import dev.shvetsova.ewmc.dto.mq.RequestStatusMqDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto addParticipationRequest(String userId, long eventId);

    List<ParticipationRequestDto> getEventParticipants(String userId, long eventId);

    List<ParticipationRequestDto> getUserRequests(String userId);

    ParticipationRequestDto cancelRequest(String userId, long requestId);

    List<ParticipationRequestDto> changeVisibilityEventParticipation(String userId, List<Long> ids, boolean hide);

    boolean isExistByRequester(String userId);

    List<Long> getFriendsEventRequests(String userId, List<Long> friendsId);

    void changeStatusRequests(RequestStatusMqDto payload);

    ParticipationRequestDto getUserRequest(String userId, long id);
}
