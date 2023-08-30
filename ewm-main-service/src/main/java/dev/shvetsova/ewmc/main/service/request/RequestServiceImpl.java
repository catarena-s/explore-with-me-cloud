package dev.shvetsova.ewmc.main.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.shvetsova.ewmc.main.dto.request.EventRequestStatusUpdateRequest;
import dev.shvetsova.ewmc.main.dto.request.EventRequestStatusUpdateResult;
import dev.shvetsova.ewmc.main.dto.request.ParticipationRequestDto;
import dev.shvetsova.ewmc.main.enums.EventState;
import dev.shvetsova.ewmc.main.enums.RequestStatus;
import dev.shvetsova.ewmc.main.exception.ConflictException;
import dev.shvetsova.ewmc.main.exception.NotFoundException;
import dev.shvetsova.ewmc.main.mapper.RequestMapper;
import dev.shvetsova.ewmc.main.model.Event;
import dev.shvetsova.ewmc.main.model.Request;
import dev.shvetsova.ewmc.main.model.User;
import dev.shvetsova.ewmc.main.repository.EventRepository;
import dev.shvetsova.ewmc.main.repository.RequestRepository;
import dev.shvetsova.ewmc.main.service.event.EventService;
import dev.shvetsova.ewmc.main.service.user.UserService;
import dev.shvetsova.ewmc.main.utils.Constants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static dev.shvetsova.ewmc.main.enums.RequestStatus.CANCELED;
import static dev.shvetsova.ewmc.main.enums.RequestStatus.CONFIRMED;
import static dev.shvetsova.ewmc.main.enums.RequestStatus.PENDING;
import static dev.shvetsova.ewmc.main.enums.RequestStatus.REJECTED;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    private final UserService userService;
    private final EventService eventService;

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
        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException(
                    String.format("Request for eventId=%d from userId=%d already exist.", eventId, userId),
                    "Conflict Exception");
        }

        final Event event = eventService.findEventById(eventId);
//        инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)<p>
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException(
                    String.format("UserId=%d is initiator for event with id=%d", userId, eventId),
                    "Conflict Exception");
        }
//        нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)<p>
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException(String.format("Event id=%d is not published", eventId),
                    "ConflictException");
        }
//        если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)<p>
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ConflictException("Event confirmed limit reached.", "Conflict exception");
        }
//        - если для события отключена пре-модерация запросов на участие,
//        то запрос должен автоматически перейти в состояние подтвержденного
        final User user = userService.findUserById(userId);

        final Request newRequest = Request.builder()
                .requester(user)
                .event(event)
                .status((event.getParticipantLimit() == 0) || (!event.getRequestModeration())
                        ? CONFIRMED
                        : PENDING
                )
                .created(LocalDateTime.now())
                .build();

        final Request savedRequest = requestRepository.save(newRequest);
        if (newRequest.getStatus().equals(CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        ParticipationRequestDto dto = RequestMapper.toDto(savedRequest);
        return dto;
    }

    @Override
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        userService.checkExistById(userId);

        final Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Request with id=%d for userId=%d was not found.", requestId, userId),
                        Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND));

        request.setStatus(CANCELED);
        requestRepository.save(request);

        ParticipationRequestDto dto = RequestMapper.toDto(request);
        return dto;
    }

    @Override
    public List<ParticipationRequestDto> changeVisibilityEventParticipation(long userId, List<Long> ids, boolean hide) {
        userService.checkExistById(userId);

        final List<Request> requestList = requestRepository.findAllById(ids);
        final boolean allMatchUser = requestList.stream()
                .allMatch(request -> request.getRequester().getId().equals(userId));
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
    public List<ParticipationRequestDto> getEventParticipants(long userId, long eventId) {
        userService.checkExistById(userId);

        final List<Request> requestList = requestRepository.findAllByEvent_InitiatorIdAndEventId(userId, eventId);

        return requestList.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(long userId) {
        userService.checkExistById(userId);

        final List<Request> requestList = requestRepository.findAllByRequesterId(userId);

        return requestList.stream().map(RequestMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
     * @param body Новый статус для заявок на участие в событии текущего пользователя
     * @param userId id текущего пользователя
     * @param eventId id события текущего пользователя
     * @return - нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)<p></p>
     * - статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)<p></p>
     * - если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
     */
    @Override
    @Transactional
    public EventRequestStatusUpdateResult changeRequestStatus(EventRequestStatusUpdateRequest body, long userId, long eventId) {
        final RequestStatus newStatus = body.getStatus();
        userService.checkExistById(userId);
        final Event event = eventService.findEventById(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
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
            requestList.forEach(r -> r.setStatus(REJECTED));
            rejectedList.addAll(requestList.stream()
                    .map(RequestMapper::toDto)
                    .toList());
        }

        if (CONFIRMED.equals(newStatus)) {
            // если для события лимит заявок равен 0 или отключена пре-модерация заявок,
            // то подтверждение заявок не требуется
            if ((participantLimit == 0) || FALSE.equals(isRequestModeration)) {
                requestList.forEach(r -> r.setStatus(CONFIRMED));
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
                    currentConfirmed++;
                } else {
                    r.setStatus(REJECTED);
                }
            }

            event.setConfirmedRequests(currentConfirmed);
            eventRepository.save(event);

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
}
