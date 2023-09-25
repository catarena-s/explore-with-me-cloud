package dev.shvetsova.ewmc.request.service.request;

import dev.shvetsova.ewmc.dto.event.EventFullDto;
import dev.shvetsova.ewmc.dto.mq.RequestMqDto;
import dev.shvetsova.ewmc.dto.mq.RequestStatusMqDto;
import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import dev.shvetsova.ewmc.dto.request.EventRequestStatusUpdateRequest;
import dev.shvetsova.ewmc.dto.request.EventRequestStatusUpdateResult;
import dev.shvetsova.ewmc.dto.request.ParticipationRequestDto;
import dev.shvetsova.ewmc.enums.SenderType;
import dev.shvetsova.ewmc.exception.ConflictException;
import dev.shvetsova.ewmc.exception.NotFoundException;
import dev.shvetsova.ewmc.request.http.EventClient;
import dev.shvetsova.ewmc.request.http.UserClient;
import dev.shvetsova.ewmc.request.mapper.RequestMapper;
import dev.shvetsova.ewmc.request.model.Request;
import dev.shvetsova.ewmc.request.model.RequestStatus;
import dev.shvetsova.ewmc.request.mq.Supplier;
import dev.shvetsova.ewmc.request.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.shvetsova.ewmc.request.model.RequestStatus.*;
import static dev.shvetsova.ewmc.utils.Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND;
import static dev.shvetsova.ewmc.utils.Constants.USER_WITH_ID_D_WAS_NOT_FOUND;
import static dev.shvetsova.ewmc.utils.UsersUtil.checkExistUser;
import static java.lang.Boolean.FALSE;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;

    private final UserClient userClient;
    private final EventClient eventClient;

    private final Supplier supplier;

    /**
     * - нельзя добавить повторный запрос (Ожидается код ошибки 409)<p>
     * - инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)<p>
     * - нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)<p>
     * - если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)<p>
     * - если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
     */
    @Override
    @Transactional
    public ParticipationRequestDto addParticipationRequest(long userId, long eventId) {
//        - нельзя добавить повторный запрос (Ожидается код ошибки 409)<p>
        boolean existUser = userClient.checkExistById(userId);
        if (!existUser) {
            throw new NotFoundException(
                    String.format(USER_WITH_ID_D_WAS_NOT_FOUND, userId),
                    "Conflict Exception");
        }
        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException(
                    String.format("Request for eventId=%d from userId=%d already exist.", eventId, userId),
                    "Conflict Exception");
        }

        final Request newRequest = Request.builder()
                .requesterId(userId)
                .eventId(eventId)
                .status(PENDING)
                .created(LocalDateTime.now())
                .build();

        final Request savedRequest = requestRepository.save(newRequest);
        supplier.sendNewMessage(new RequestMqDto(savedRequest.getRequesterId(), eventId, userId));

        return RequestMapper.toDto(savedRequest);
    }

    @Override
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        checkExistUser(userClient, userId);

        final Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Request with id=%d for userId=%d was not found.", requestId, userId),
                        THE_REQUIRED_OBJECT_WAS_NOT_FOUND));

        request.setStatus(CANCELED);
        requestRepository.save(request);
        return RequestMapper.toDto(request);
    }


    @Override
    public List<ParticipationRequestDto> changeVisibilityEventParticipation(long userId, List<Long> ids, boolean hide) {
        userClient.checkExistById(userId);

        final List<Request> requestList = requestRepository.findAllById(ids);
        final boolean allMatchUser = requestList.stream()
                .allMatch(request -> request.getRequesterId() == userId);
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
    public boolean isExistByRequester(long userId) {
        return requestRepository.existsByRequesterId(userId);
    }

    @Override
    public List<Long> getFriendsEventRequests(long userId, List<Long> friendsId) {
        userClient.checkExistById(userId);
        return requestRepository.findAllEventsForRequesters(friendsId);
    }

    @Override
    @Transactional
    public void changeStatusRequests(RequestStatusMqDto requestStatusMqDto) {
        final RequestStatus newStatus = RequestStatus.from(requestStatusMqDto.getNewStatus());
        final List<Request> requestList = requestRepository.findAllById(requestStatusMqDto.getRequest());
        final SenderType senderType = CONFIRMED.equals(newStatus)
                ? SenderType.REQUEST_CONFIRMED
                : SenderType.REQUEST_REJECTED;
        final long userId = requestStatusMqDto.getUserId();
        final long eventId = requestStatusMqDto.getEventId();
        requestList.forEach(r -> {
            r.setStatus(newStatus);
            sendNotification(userId, eventId, senderType,
                    "Changed status Participation Request to event.");
        });
        requestRepository.saveAll(requestList);
    }

    @Override
    public List<ParticipationRequestDto> getEventParticipants(long userId, long eventId) {
        userClient.checkExistById(userId);
        final List<Request> requestList = requestRepository.findAllByEventId(eventId);
        return requestList.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(long userId) {
        userClient.checkExistById(userId);

        final List<Request> requestList = requestRepository.findAllByRequesterId(userId);

        return requestList.stream().map(RequestMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
     *
     * @param body    Новый статус для заявок на участие в событии текущего пользователя
     * @param userId  id текущего пользователя
     * @param eventId id события текущего пользователя
     * @return - нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)<p></p>
     * - статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)<p></p>
     * - если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
     */
    @Override
    @Transactional
    public EventRequestStatusUpdateResult changeRequestStatus(EventRequestStatusUpdateRequest body, long userId, long eventId) {
        final RequestStatus newStatus = RequestStatus.from(body.getStatus());
        userClient.checkExistById(userId);
        final EventFullDto event = eventClient.getEventById(userId, eventId);
        if (event.getInitiator().getId() != userId) {
            throw new ConflictException(String.format("User(id=%d) is not the initiator of the event(id=%d).", userId, eventId));
        }

        final List<Long> requestIds = body.getRequestIds();

        final long participantLimit = event.getParticipantLimit();
        int currentConfirmed = event.getConfirmedRequests();
        final Boolean isRequestModeration = event.getRequestModeration();
        if (participantLimit > 0 && participantLimit == currentConfirmed) {
            // нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
            // (Ожидается код ошибки 409)
            throw new ConflictException(
                    "The limit on confirmations for this event has already been reached.",
                    "Conflict confirmed exception");
        }
        //находим заявки в режиме ожидания по списку id
        final List<Request> requestList = requestRepository.findAllByIdInAndStatus(requestIds, PENDING);
        if (requestList.size() != requestIds.size()) {
            // статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
            throw new ConflictException("Status change is only possible for requests with state='PENDING'");
        }

        final List<ParticipationRequestDto> rejectedList = new ArrayList<>();
        final List<ParticipationRequestDto> confirmedDto = new ArrayList<>();


        checkStatus(newStatus);

        if (REJECTED.equals(newStatus)) {
            requestList.forEach(r -> {
                r.setStatus(REJECTED);
                sendNotification(event.getInitiator().getId(), eventId, SenderType.REQUEST_REJECTED, "Rejected Participation Request to event.");
            });
            rejectedList.addAll(requestList.stream()
                    .map(RequestMapper::toDto)
                    .toList());


        }

        if (CONFIRMED.equals(newStatus)) {
            // если для события лимит заявок равен 0 или отключена пре-модерация заявок,
            // то подтверждение заявок не требуется
            if ((participantLimit == 0) || FALSE.equals(isRequestModeration)) {
                requestList.forEach(r -> {
                    r.setStatus(CONFIRMED);

                });
                final List<ParticipationRequestDto> confirmedList = requestList.stream()
                        .map(RequestMapper::toDto)
                        .collect(Collectors.toList());
                requestRepository.saveAll(requestList);
                return new EventRequestStatusUpdateResult(confirmedList, Collections.emptyList());
            }
            // - если при подтверждении данной заявки, лимит заявок для события исчерпан,
            // то все неподтверждённые заявки необходимо отклонить

            for (Request r : requestList) {
                if (currentConfirmed < participantLimit) {
                    r.setStatus(CONFIRMED);
                    sendNotification(event.getInitiator().getId(), eventId, SenderType.REQUEST_CONFIRMED, "Confirmed Participation Request to event.");
                    currentConfirmed++;
                } else {
                    r.setStatus(REJECTED);
                    sendNotification(event.getInitiator().getId(), eventId, SenderType.REQUEST_REJECTED, "Rejected Participation Request to event.");
                }
            }

            event.setConfirmedRequests(currentConfirmed);
            eventClient.upConfirmedRequests(userId, eventId);

            confirmedDto.addAll(requestList.stream()
                    .filter(RequestServiceImpl::isConfirmedRequest)
                    .map(RequestMapper::toDto)
                    .toList());
            rejectedList.addAll(requestList.stream()
                    .filter(RequestServiceImpl::isRejectedRequest)
                    .map(RequestMapper::toDto)
                    .toList());
        }
        requestRepository.saveAll(requestList);
        return new EventRequestStatusUpdateResult(confirmedDto, rejectedList);
    }

    private void checkStatus(RequestStatus newStatus) {
        final Set<RequestStatus> availableStats = Set.of(CONFIRMED, REJECTED);
        if (!availableStats.contains(newStatus)) {
            throw new ConflictException(String.format("Wrong status. Status should be one of: %s",
                    availableStats.stream().sorted().collect(Collectors.toList())));
        }
    }


    private static boolean isConfirmedRequest(Request r) {
        return CONFIRMED.equals(r.getStatus());
    }

    private static boolean isRejectedRequest(Request r) {
        return REJECTED.equals(r.getStatus());
    }

    private void sendNotification(Long userId, long senderId, SenderType senderType, String text) {
        supplier.sendNewMessage(NewNotificationDto.builder()
                .consumerId(userId)
                .senderId(senderId)
                .messageType(senderType)
                .text(text)
                .build());
    }
}
