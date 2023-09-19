package dev.shvetsova.ewmc.request.service.request;

import dev.shvetsova.ewmc.dto.event.EventFullDto;
import dev.shvetsova.ewmc.dto.mq.RequestMqDto;
import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import dev.shvetsova.ewmc.dto.request.ParticipationRequestDto;
import dev.shvetsova.ewmc.enums.MessageType;
import dev.shvetsova.ewmc.exception.ConflictException;
import dev.shvetsova.ewmc.exception.NotFoundException;
import dev.shvetsova.ewmc.request.http.EventClient;
import dev.shvetsova.ewmc.request.http.UserClient;
import dev.shvetsova.ewmc.request.mapper.RequestMapper;
import dev.shvetsova.ewmc.request.model.Request;
import dev.shvetsova.ewmc.request.model.RequestStatus;
import dev.shvetsova.ewmc.request.mq.NotificationSupplier;
import dev.shvetsova.ewmc.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static dev.shvetsova.ewmc.request.model.RequestStatus.*;
import static dev.shvetsova.ewmc.utils.Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    private final UserClient userClient;
    private final EventClient eventClient;

    private final NotificationSupplier notificationSupplier;

    /**
     * - нельзя добавить повторный запрос (Ожидается код ошибки 409)<p>
     * - инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)<p>
     * - нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)<p>
     * - если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)<p>
     * - если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
     */
    @Override
    @Transactional
    public ParticipationRequestDto addParticipationRequest(String userId, long eventId) {
//        - нельзя добавить повторный запрос (Ожидается код ошибки 409)<p>
        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException(
                    String.format("Request for eventId=%d from userId=%s already exist.", eventId, userId),
                    "Conflict Exception");
        }

//        final EventFullDto event = eventClient.getEventById(12L, eventId);

//        инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)<p>
//        if (event.getInitiator().getId().equals(userId)) {
//            throw new ConflictException(
//                    String.format("UserId=%s is initiator for event with id=%d", userId, eventId),
//                    "Conflict Exception");
//        }
//        нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)<p>
//        if (!event.getState().equals("PUBLISHED")) {
//            throw new ConflictException(String.format("Event id=%d is not published", eventId),
//                    "ConflictException");
//        }
//        если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)<p>
//        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
//            throw new ConflictException("Event confirmed limit reached.", "Conflict exception");
//        }
//        - если для события отключена пре-модерация запросов на участие,
//        то запрос должен автоматически перейти в состояние подтвержденного

        final Request newRequest = Request.builder()
                .requesterId(userId)
                .eventId(eventId)
                .status(/*(event.getParticipantLimit() == 0) || (!event.getRequestModeration())
                        ? CONFIRMED
                        :*/ PENDING
                )
                .created(LocalDateTime.now())
                .build();

        final Request savedRequest = requestRepository.save(newRequest);
        if (newRequest.getStatus().equals(CONFIRMED)) {
//            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventClient.upConfirmedRequests(12L, eventId);
        } else {
            sendNotification("event.getInitiator().getId()", String.valueOf(eventId), MessageType.REQUEST, "Get Participation Request to event.");
        }

        return RequestMapper.toDto(savedRequest);
    }

    @Override
    public ParticipationRequestDto cancelRequest(String userId, long requestId) {
        final Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Request with id=%d for userId=%s was not found.", requestId, userId),
                        THE_REQUIRED_OBJECT_WAS_NOT_FOUND));

        request.setStatus(CANCELED);
        requestRepository.save(request);
        return RequestMapper.toDto(request);
    }


    @Override
    public List<ParticipationRequestDto> changeVisibilityEventParticipation(String userId, List<Long> ids, boolean hide) {
        final List<Request> requestList = requestRepository.findAllById(ids);
        final boolean allMatchUser = requestList.stream()
                .allMatch(request -> request.getRequesterId().equals(userId));
        if (!allMatchUser) {
            throw new ConflictException("User cannot change the visibility of events in which he does not participate.");
        }
        final boolean allMatchStatus = requestList.stream()
                .allMatch(request -> request.getStatus().equals(CONFIRMED));
        if (!allMatchStatus) {
            throw new ConflictException("Participation in events must be confirmed.");
        }
        requestList.forEach(f -> f.setPrivate(hide));
        requestRepository.saveAll(requestList);
        return requestList.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isExistByRequester(String userId) {
        return requestRepository.existsByRequesterId(userId);
    }

    @Override
    public List<Long> getFriendsEventRequests(String userId, List<Long> friendsId) {
        return requestRepository.findAllEventsForRequesters(friendsId);
    }

    @Override
    @Transactional
    public void changeStatusRequests(RequestMqDto requestMqDto) {
        final RequestStatus newStatus = RequestStatus.from(requestMqDto.getNewStatus());
        final List<Request> requestList = requestRepository.findAllById(requestMqDto.getRequest());
        final MessageType messageType = CONFIRMED.equals(newStatus) ? MessageType.REQUEST_CONFIRMED : MessageType.REQUEST_REJECTED;
        final String userId = requestMqDto.getUserId();
        final long eventId = requestMqDto.getEventId();
        requestList.forEach(r -> {
            r.setStatus(newStatus);
            sendNotification(userId, String.valueOf(eventId), messageType, "Changed status Participation Request to event.");
        });
        requestRepository.saveAll(requestList);
    }

    @Override
    public List<ParticipationRequestDto> getEventParticipants(String userId, long eventId) {
        final List<Request> requestList = requestRepository.findAllByEventId(eventId);
        return requestList.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(String userId) {
        final List<Request> requestList = requestRepository.findAllByRequesterId(userId);
        return requestList.stream().map(RequestMapper::toDto).collect(Collectors.toList());
    }

    private void sendNotification(String userId, String senderId, MessageType messageType, String text) {
        notificationSupplier.sendNewMessage(NewNotificationDto.builder()
                .userId(userId)
                .senderId(senderId)
                .messageType(messageType)
                .text(text)
                .build());
    }
}
