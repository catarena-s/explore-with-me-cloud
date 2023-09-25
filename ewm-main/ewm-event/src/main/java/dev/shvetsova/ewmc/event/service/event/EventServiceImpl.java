package dev.shvetsova.ewmc.event.service.event;

import com.querydsl.core.types.Predicate;
import dev.shvetsova.ewmc.dto.event.*;
import dev.shvetsova.ewmc.dto.location.LocationDto;
import dev.shvetsova.ewmc.dto.mq.EventInfoMq;
import dev.shvetsova.ewmc.dto.mq.RequestMqDto;
import dev.shvetsova.ewmc.dto.mq.RequestStatusMqDto;
import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import dev.shvetsova.ewmc.dto.request.EventRequestStatusUpdateRequest;
import dev.shvetsova.ewmc.enums.SenderType;
import dev.shvetsova.ewmc.event.enums.EventState;
import dev.shvetsova.ewmc.event.enums.EventStateAction;
import dev.shvetsova.ewmc.event.enums.SortType;
import dev.shvetsova.ewmc.event.http.RequestClient;
import dev.shvetsova.ewmc.event.http.UserClient;
import dev.shvetsova.ewmc.event.mapper.EventMapper;
import dev.shvetsova.ewmc.event.model.Category;
import dev.shvetsova.ewmc.event.model.Event;
import dev.shvetsova.ewmc.event.model.Location;
import dev.shvetsova.ewmc.event.mq.EventSupplier;
import dev.shvetsova.ewmc.event.repo.EventRepository;
import dev.shvetsova.ewmc.event.service.category.CategoryService;
import dev.shvetsova.ewmc.event.service.location.LocationService;
import dev.shvetsova.ewmc.event.service.stats.StatsService;
import dev.shvetsova.ewmc.event.utils.QPredicate;
import dev.shvetsova.ewmc.event.utils.filter.EventFilter;
import dev.shvetsova.ewmc.event.utils.filter.EventPredicate;
import dev.shvetsova.ewmc.exception.ConflictException;
import dev.shvetsova.ewmc.exception.NotFoundException;
import dev.shvetsova.ewmc.exception.ValidateException;
import dev.shvetsova.ewmc.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static dev.shvetsova.ewmc.event.enums.EventStateAction.*;
import static dev.shvetsova.ewmc.utils.UsersUtil.checkExistUser;
import static java.lang.Boolean.FALSE;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final String EVENT_DATE_AND_TIME_IS_BEFORE = "Event date and time cannot be earlier than %d hours from the";
    private final EventRepository eventRepository;

    private final UserClient userClient;
    private final RequestClient requestClient;

    private final CategoryService categoryService;
    private final LocationService locationService;
    private final StatsService statsService;

    private final EventSupplier supplier;

    @Override
    @Transactional
    public EventFullDto saveEvent(long userId, NewEventDto body) {
//        дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
        confirmEventDateIsAfterCurrent(body.getEventDate(), 2);
        checkExistUser(userClient, userId);
        final Category category = categoryService.findCategoryById(body.getCategory());
        final Location location = locationService.findLocation(body.getLocation());

        final Event event = EventMapper.fromDto(body, userId, category, location, EventState.PENDING, LocalDateTime.now());
        eventRepository.save(event);

        return EventMapper.toFullDto(event);
    }

    @Override
    public List<EventShortDto> getPublishedEvents(long userId, int from, int size) {
        checkExistUser(userClient, userId);
        final PageRequest page = PageRequest.of(from / size, size);
        final List<Event> events = eventRepository.findAllByInitiatorId(userId, page).getContent();
        return events.stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(long eventId) {
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constants.EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND));
        return EventMapper.toFullDto(event);
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               Integer from, Integer size) {

        confirmStartBeforeEnd(rangeStart, rangeEnd);

        final List<EventState> stateList = (states == null) ? null : getEventStates(states);
        final PageRequest page = PageRequest.of(from / size, size);
        final EventFilter filter = EventFilter.builder()
                .initiatorIn(users)
                .categoryIn(categories)
                .eventDateAfter(rangeStart)
                .eventDateBefore(rangeEnd)
                .statesIn(stateList)
                .build();
        final Predicate predicate = EventPredicate.getAndEventPredicate(filter);

        final List<Event> events = (predicate == null)
                ? eventRepository.findAll(page).getContent()
                : eventRepository.findAll(predicate, page).getContent();
        return events.stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }


    /**
     * Получение подробной информации об опубликованном событии по его идентификатору<br>
     * - событие должно быть опубликовано <br>
     * - информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов <br>
     * - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики <br>
     */
    @Override
    public EventFullDto getPublishedEvent(long eventId, HttpServletRequest request) {
        final Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constants.EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND));

        statsService.save(request);
        final Map<String, Long> mapViewStats = statsService.getMap(request, true);
        return EventMapper.toFullDto(event, mapViewStats.getOrDefault(request.getRequestURI(), 0L));
    }

    /**
     * - в выдаче должны быть только опубликованные события<br>
     * - текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв<br>
     * - если в запросе не указан диапазон дат [rangeStart-rangeEnd],
     * то нужно выгружать события, которые произойдут позже текущей даты и времени<br>
     * - информация о каждом событии должна включать в себя количество просмотров и
     * количество уже одобренных заявок на участие<br>
     * - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
     * нужно сохранить в сервисе статистики<br>
     */
    @Override
    public List<EventShortDto> getPublishedEvents(String text, List<Long> categories, Boolean paid,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                  Boolean onlyAvailable,
                                                  SortType sort,//по дате события или по количеству просмотров
                                                  Integer from, Integer size, HttpServletRequest request) {
        confirmStartBeforeEnd(rangeStart, rangeEnd);

        final PageRequest page = getPageRequest(sort, from, size);

        final List<Predicate> predicateList = getPredicates(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        final Predicate predicate = QPredicate.buildAnd(predicateList);
        final List<Event> events = eventRepository.findAll(predicate, page).getContent();

        statsService.save(request);
        if (events.isEmpty()) return Collections.emptyList();


        final Map<String, Long> mapViewStats = getViewStats(request, events, false);
        List<EventShortDto> eventShortDtoList = events.stream()
                .map(event -> EventMapper.toShortDto(event, getView(request, mapViewStats, event.getId())))
                .collect(Collectors.toList());

        return needSortByViews(sort)
                ? getSortedList(eventShortDtoList)
                : eventShortDtoList;

    }

    @Override
    @Transactional
    //Изменение события добавленного текущим пользователем privet api
    public EventFullDto updateEventByUser(UpdateEventUserRequest body, long userId, long eventId) {
        userClient.checkExistById(userId);
        final Event event = getEventForUser(userId, eventId);

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event state is Published", "ConflictException");
        }
        updateData(event, body.getAnnotation(),
                body.getTitle(),
                body.getDescription(),
                body.getParticipantLimit(),
                body.getPaid(),
                body.getRequestModeration()
        );

        updateEventDate(body.getEventDate(), event, 1);
        updateEventCategory(body.getCategory(), event, categoryService);
        updateLocation(body.getLocation(), event, locationService);

        updateStatusByUser(body, event);

        final Event savedEvent = eventRepository.save(event);

        if (body.getStateAction() != null && PUBLISH_EVENT.name().equals(body.getStateAction())) {
            supplier.newEventFromUserProduce(new EventInfoMq(userId, event.getId()));
        }
        return EventMapper.toFullDto(savedEvent);
    }

    @Override
    public List<EventShortDto> getPublishedEvents(long userId, List<Long> friendsId) {
        userClient.checkExistById(userId);
        final List<Event> events = eventRepository.findAllByInitiatorIdInAndState(friendsId, EventState.PUBLISHED);
        return events.isEmpty()
                ? Collections.emptyList()
                : events.stream().map(EventMapper::toShortDto).toList();
    }

    @Override
    public List<EventShortDto> getParticipateEventList(long userId, List<Long> friendsId) {
        userClient.checkExistById(userId);
        List<Long> eventList = requestClient.getParticipateEventList(userId, friendsId);
        final List<Event> events = eventRepository.findAllByIdInAndState(eventList, EventState.PUBLISHED);
        return events.isEmpty()
                ? Collections.emptyList()
                : events.stream().map(EventMapper::toShortDto).toList();
    }

    @Override
    @Transactional
    public void upConfirmedRequests(long userId, long eventId) {
        userClient.checkExistById(userId);
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND)
        );
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
    }

    @Override
    @Transactional
    public void changeRequestStatus(EventRequestStatusUpdateRequest body, long userId, long eventId) {
        userClient.checkExistById(userId);
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND)
        );

        if (event.getInitiatorId() != userId) {
            throw new ConflictException(String.format("User(id=%d) is not the initiator of the event(id=%d).", userId, eventId));
        }
        String newStatus = body.getStatus();
        if ("REJECTED".equals(newStatus)) {
            supplier.changeStatusRequests(RequestStatusMqDto.builder()
                    .userId(userId)
                    .eventId(eventId)
                    .request(body.getRequestIds())
                    .newStatus(newStatus)
                    .build());
            return;
        }
        if ("CONFIRMED".equals(newStatus)) {
            final long participantLimit = event.getParticipantLimit();
            int currentConfirmed = event.getConfirmedRequests();
            // нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
            // (Ожидается код ошибки 409)
            if (participantLimit > 0 && participantLimit == currentConfirmed) {
                throw new ConflictException(
                        "The limit on confirmations for this event has already been reached.",
                        "Conflict confirmed exception");
            }

            final Boolean isRequestModeration = event.getRequestModeration();
            if ((participantLimit == 0) || FALSE.equals(isRequestModeration)) {
                supplier.changeStatusRequests(RequestStatusMqDto.builder()
                        .userId(userId)
                        .eventId(eventId)
                        .request(body.getRequestIds())
                        .newStatus(newStatus)
                        .build());
                return;
            }
            // - если при подтверждении данной заявки, лимит заявок для события исчерпан,
            // то все неподтверждённые заявки необходимо отклонить
            final List<Long> rejectedRequests = new ArrayList<>();
            final List<Long> confirmedRequests = new ArrayList<>();
            for (Long requestId : body.getRequestIds()) {
                if (currentConfirmed < participantLimit) {
                    confirmedRequests.add(requestId);
                    currentConfirmed++;
                } else {
                    rejectedRequests.add(requestId);
                }
            }
            event.setConfirmedRequests(currentConfirmed);
            if (!confirmedRequests.isEmpty()) {
                supplier.changeStatusRequests(RequestStatusMqDto.builder()
                        .userId(userId)
                        .eventId(eventId)
                        .request(confirmedRequests)
                        .newStatus("CONFIRMED")
                        .build());
            }
            if (!rejectedRequests.isEmpty()) {
                supplier.changeStatusRequests(RequestStatusMqDto.builder()
                        .userId(userId)
                        .eventId(eventId)
                        .request(rejectedRequests)
                        .newStatus("REJECTED")
                        .build());
            }
            eventRepository.save(event);
        }
    }

    @Override
    @Transactional
    public void getRequest(RequestMqDto request) {
        Long eventId = request.getEventId();
        Long requestId = request.getRequestId();
        Long userId = request.getUserId();
        Event event = findEventById(eventId);

        if (event.getInitiatorId() == userId) {
            throw new ConflictException(
                    String.format("UserId=%d is initiator for event with id=%d", userId, eventId),
                    "Conflict Exception");
        }

//        нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)<p>
        if (!event.getState().equals("PUBLISHED")) {
            throw new ConflictException(String.format("Event id=%d is not published", eventId),
                    "ConflictException");
        }
//        если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)<p>
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ConflictException("Event confirmed limit reached.", "Conflict exception");
        }

        if ((event.getParticipantLimit() == 0) || (!event.getRequestModeration())) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            supplier.changeStatusRequests(new RequestStatusMqDto(List.of(requestId), userId, eventId, "CONFIRMED"));
            return;
        }
        supplier.sendNewMessage(NewNotificationDto.builder()
                .consumerId(userId)
                .senderId(requestId)
                .messageType(SenderType.REQUEST)
                .text("Event request")
                .build());
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(UpdateEventAdminRequest body, long eventId) {
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND)
        );

        updateData(event,
                body.getAnnotation(),
                body.getTitle(),
                body.getDescription(),
                body.getParticipantLimit(),
                body.getPaid(),
                body.getRequestModeration()
        );

        updateEventDate(body.getEventDate(), event, 2);
        updateEventCategory(body.getCategory(), event, categoryService);
        updateLocation(body.getLocation(), event, locationService);
        updateStatusByAdmin(body, event);

        final Event savedEvent = eventRepository.save(event);

        if (body.getStateAction() != null && PUBLISH_EVENT.name().equals(body.getStateAction())) {
            supplier.newEventFromUserProduce(new EventInfoMq(event.getInitiatorId(), event.getId()));
        }
        return EventMapper.toFullDto(savedEvent);
    }

    @Override
    public Event findEventById(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constants.EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND));
    }

    @Override
    public List<EventShortDto> findEventsByIds(List<Long> eventIdList) {
        return eventRepository.findAllById(eventIdList).stream().map(EventMapper::toShortDto).toList();
    }

    @Override
    public boolean isExistByInitiator(long userId) {
        return eventRepository.existsByInitiatorId(userId);
    }

    private Map<String, Long> getViewStats(HttpServletRequest request, List<Event> events, boolean unique) {
        final LocalDateTime start = events.stream()
                .map(Event::getPublishedOn)
                .min(LocalDateTime::compareTo)
                .orElse(Constants.START);
        final LocalDateTime end = LocalDateTime.now();
        final List<Long> collect = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        return statsService.getMap(request, collect, start, end, unique);
    }

    private boolean needSortByViews(SortType sort) {
        return sort != null && sort.equals(SortType.VIEWS);
    }

    private List<Predicate> getPredicates(String text,
                                          List<Long> categories,
                                          Boolean paid,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          Boolean onlyAvailable) {
        List<Predicate> predicateList = new ArrayList<>();

        final EventFilter mainFilter = EventFilter.builder()
                .paidEq(paid)
                .categoryIn(categories)
                .eventDateAfter(rangeStart)
                .eventDateBefore(rangeEnd)
                .stateEq(EventState.PUBLISHED)
                .build();

        final Predicate mainPredicate = EventPredicate.getAndEventPredicate(mainFilter);
        predicateList.add(mainPredicate);

        if (text != null && !text.isBlank()) {
            predicateList.add(EventPredicate.getTextFilter(text));
        }

        if (onlyAvailable != null && onlyAvailable) {
            predicateList.add(EventPredicate.getAvailable());
        }
        return predicateList;
    }

    private PageRequest getPageRequest(SortType sort, Integer from, Integer size) {
        return (sort == null)
                ? PageRequest.of(from / size, size)
                : getPageRequestWithSort(from, size, sort);
    }

    private PageRequest getPageRequestWithSort(Integer from, Integer size, SortType sortType) {
        return sortType.equals(SortType.VIEWS)
                ? PageRequest.of(from / size, size)
                : PageRequest.of(from / size, size).withSort(Sort.by(sortType.getName()));
    }

    /**
     * Получение списка статусов
     */
    private List<EventState> getEventStates(List<String> states) {
        return states.stream()
                .map(EventState::from)
                .collect(Collectors.toList());
    }

    /**
     * Получить просмотры события
     */
    private static long getView(HttpServletRequest request, Map<String, Long> viewStats, Long eventId) {
        final String uri = request.getRequestURI() + "/" + eventId;
        return viewStats.getOrDefault(uri, 0L);
    }

    /**
     * Получить событие пользователя
     */
    private Event getEventForUser(long userId, long eventId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constants.EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND));
    }

    private static void updateData(Event event, String annotation,
                                   String title, String description,
                                   Integer participantLimit,
                                   Boolean paid,
                                   Boolean requestModeration) {
        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        if (title != null) {
            event.setTitle(title);
        }
        if (description != null) {
            event.setDescription(description);
        }
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }
        if (paid != null) {
            event.setPaid(paid);
        }
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
    }

    /**
     * Обновление статуса для public api
     */
    private void updateStatusByUser(UpdateEventUserRequest body, Event event) {
        final EventStateAction newEventState = from(body.getStateAction());
        if (newEventState == null) return;

        final Set<EventStateAction> availableStats = Set.of(CANCEL_REVIEW, SEND_TO_REVIEW);
        checkStatus(availableStats, newEventState);
        event.setState(newEventState.getEventState());
    }

    /**
     * Обновление статуса admin api
     */
    private void updateStatusByAdmin(UpdateEventAdminRequest body, Event event) {
        final EventStateAction newEventState = from(body.getStateAction());
        if (newEventState == null) return;

        final Set<EventStateAction> availableStats = Set.of(PUBLISH_EVENT, REJECT_EVENT);
        checkStatus(availableStats, newEventState);

        final EventState currentEventState = event.getState();
        if (PUBLISH_EVENT.equals(newEventState)) {
            throwIfNotAvailableStatus(Set.of(EventState.PUBLISHED, EventState.CANCELED), currentEventState, newEventState);
            event.setPublishedOn(LocalDateTime.now());
        } else if (REJECT_EVENT.equals(newEventState)) {
            throwIfNotAvailableStatus(Set.of(EventState.PUBLISHED, EventState.CANCELED), currentEventState, newEventState);
        }
        event.setState(newEventState.getEventState());
    }

    private void updateEventDate(LocalDateTime newEventDate, Event event, int difHour) {
        if (newEventDate != null) {
            confirmEventDateIsAfterCurrent(newEventDate, difHour);
            if (EventState.PUBLISHED.equals(event.getState())) {
                LocalDateTime publishedDate = event.getPublishedOn();
                confirmEventDateIsAfterPublished(newEventDate, publishedDate, difHour);
                event.setEventDate(newEventDate);
            }
            event.setEventDate(newEventDate);
        }
    }

    private void updateEventCategory(Long categoryId, Event event, CategoryService service) {
        if (categoryId != null) {
            final Category category = service.findCategoryById(categoryId);
            event.setCategory(category);
        }
    }

    private void updateLocation(LocationDto locationDto, Event event, LocationService service) {
        if (locationDto != null) {
            final Location location = service.findLocation(locationDto);
            event.setLocation(location);
        }
    }

    private void confirmStartBeforeEnd(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidateException("'rangeStart' must be before 'rangeEnd'");
        }
    }

    private void confirmEventDateIsAfterCurrent(LocalDateTime eventDate, int hours) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (eventDate.isBefore(currentTime.plusHours(hours))) {
            throw new ValidateException(
                    String.format(EVENT_DATE_AND_TIME_IS_BEFORE + " current moment.", hours),
                    "Validate exception");
        }
    }

    private void confirmEventDateIsAfterPublished(LocalDateTime eventDate, LocalDateTime published, int hours) {
        if (eventDate.isBefore(published.plusHours(hours))) {
            throw new ValidateException(
                    String.format(EVENT_DATE_AND_TIME_IS_BEFORE + " published moment.", hours),
                    "Validate exception");
        }
    }

    private void checkStatus(Set<EventStateAction> set, EventStateAction newEventState) {
        if (!set.contains(newEventState)) {
            throw new ConflictException(String.format("Wrong status. Status should be one of: %s",
                    set.stream().sorted().collect(Collectors.toList())));
        }
    }

    private void throwIfNotAvailableStatus(Set<EventState> set, EventState currentEventState, EventStateAction newEventState) {
        if (set.contains(currentEventState)) {
            throw new ConflictException(String.format(Constants.IMPOSSIBLE_S_WHEN_EVENT_STATUS_ONE_OF_S_CURRENT_STATUS_S,
                    newEventState, set.stream().sorted().collect(Collectors.toList()), currentEventState));
        }
    }

    private List<EventShortDto> getSortedList(List<EventShortDto> eventShortDtoList) {
        return eventShortDtoList.stream()
                .sorted(Comparator.comparing(EventShortDto::getViews))
                .collect(Collectors.toList());
    }
}
