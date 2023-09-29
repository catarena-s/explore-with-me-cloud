package dev.shvetsova.ewmc.event.service.event;

import com.querydsl.core.types.Predicate;
import dev.shvetsova.ewmc.dto.event.*;
import dev.shvetsova.ewmc.dto.location.LocationDto;
import dev.shvetsova.ewmc.dto.mq.EventInfoMq;
import dev.shvetsova.ewmc.dto.mq.RequestMqDto;
import dev.shvetsova.ewmc.dto.mq.RequestStatusMqDto;
import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import dev.shvetsova.ewmc.dto.request.EventRequestStatusUpdateRequest;
import dev.shvetsova.ewmc.enums.MessageType;
import dev.shvetsova.ewmc.event.enums.EventState;
import dev.shvetsova.ewmc.event.enums.EventStateAction;
import dev.shvetsova.ewmc.event.enums.SortType;
import dev.shvetsova.ewmc.event.http.RequestClient;
import dev.shvetsova.ewmc.event.mapper.EventMapper;
import dev.shvetsova.ewmc.event.model.Category;
import dev.shvetsova.ewmc.event.model.Event;
import dev.shvetsova.ewmc.event.model.Location;
import dev.shvetsova.ewmc.event.mq.EventSupplier;
import dev.shvetsova.ewmc.event.mq.NotificationSupplier;
import dev.shvetsova.ewmc.event.mq.RequestsSupplier;
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

import static java.lang.Boolean.FALSE;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final String EVENT_DATE_AND_TIME_IS_BEFORE = "Event date and time cannot be earlier than %d hours from the";
    public static final String REJECTED = "REJECTED";
    public static final String CONFIRMED = "CONFIRMED";
    private final EventRepository eventRepository;

    private final RequestClient requestClient;

    private final CategoryService categoryService;
    private final LocationService locationService;
    private final StatsService statsService;

    private final RequestsSupplier requestsSupplier;
    private final EventSupplier eventSupplier;
    private final NotificationSupplier notificationSupplier;


    @Override
    @Transactional
    public EventFullDto saveEvent(String userId, NewEventDto body) {
//        дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
        confirmEventDateIsAfterCurrent(body.getEventDate(), 2);
        final Category category = categoryService.findCategoryById(body.getCategory());
        final Location location = locationService.findLocation(body.getLocation());

        final Event event = EventMapper.fromDto(body, userId, category, location, EventState.PENDING, LocalDateTime.now());
        eventRepository.save(event);
        eventSupplier.sendMessageToQueue(new EventInfoMq(userId, event.getId()));
        return EventMapper.toFullDto(event);
    }

    @Override
    public List<EventShortDto> getPublishedEvents(String userId, int from, int size) {
        final PageRequest page = PageRequest.of(from / size, size);
        final List<Event> events = eventRepository.findAllByInitiatorId(userId, page).getContent();
        return events.stream()
                .map(EventMapper::toShortDto)
                .toList();
    }

    @Override
    public EventFullDto getEventById(String userId, long eventId) {
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constants.EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND));
        return EventMapper.toFullDto(event);
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<String> users, List<String> states, List<Long> categories,
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
                .toList();
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
                .toList();

        return needSortByViews(sort)
                ? getSortedList(eventShortDtoList)
                : eventShortDtoList;

    }

    @Override
    @Transactional
    //Изменение события добавленного текущим пользователем privet api
    public EventFullDto updateEventByUser(UpdateEventUserRequest body, String userId, long eventId) {
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
        return EventMapper.toFullDto(savedEvent);
    }

    @Override
    public List<EventShortDto> getPublishedEvents(List<Long> friendsId) {
        final List<Event> events = eventRepository.findAllByInitiatorIdInAndState(friendsId, EventState.PUBLISHED);
        return events.isEmpty()
                ? Collections.emptyList()
                : events.stream().map(EventMapper::toShortDto).toList();
    }

    @Override
    public List<EventShortDto> getParticipateEventList(long userId, List<Long> friendsId) {
        List<Long> eventList = requestClient.getParticipateEventList(friendsId);
        final List<Event> events = eventRepository.findAllByIdInAndState(eventList, EventState.PUBLISHED);
        return events.isEmpty()
                ? Collections.emptyList()
                : events.stream().map(EventMapper::toShortDto).toList();
    }

    @Override
    @Transactional
    public void upConfirmedRequests(String userId, long eventId) {
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND)
        );
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
    }

    @Override
    @Transactional
    public void changeRequestStatus(EventRequestStatusUpdateRequest body, String userId, long eventId) {
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(Constants.EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND)
                );

        if (!event.getInitiatorId().equals(userId)) {
            throw new ConflictException(String.format("User(id=%s) is not the initiator of the event(id=%d).", userId, eventId));
        }
        final String newStatus = body.getStatus();
        if (REJECTED.equals(newStatus)) {
            sendChangedRequestStatus(userId, eventId, body.getRequestIds(), newStatus);
            return;
        }
        if (CONFIRMED.equals(newStatus)) {
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
                sendChangedRequestStatus(userId, eventId, body.getRequestIds(), newStatus);
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
                sendChangedRequestStatus(userId, eventId, confirmedRequests, CONFIRMED);
            }
            if (!rejectedRequests.isEmpty()) {
                sendChangedRequestStatus(userId, eventId, rejectedRequests, REJECTED);
            }
            eventRepository.save(event);
        }
    }

    @Override
    @Transactional
    public void getNewRequest(RequestMqDto payload) {
        final Long eventId = payload.getEventId();
        final String userId = payload.getUserId();
        final Long requestId = payload.getRequestId();
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND)
        );

        final long participantLimit = event.getParticipantLimit();
        final Boolean requestModeration = event.getRequestModeration();
        final int currentConfirmed = event.getConfirmedRequests();
        if (participantLimit > 0 && (participantLimit == currentConfirmed)) {
            sendChangedRequestStatus(userId, eventId, List.of(requestId), REJECTED);
            return;
        }
        if (participantLimit == 0 && FALSE.equals(requestModeration)) {
            sendChangedRequestStatus(userId, eventId, List.of(requestId), CONFIRMED);
            event.setConfirmedRequests(currentConfirmed + 1);
            eventRepository.save(event);
            return;
        }
        notificationSupplier.sendMessageToQueue(NewNotificationDto.builder()
                .text("You get request to event")
                .messageType(MessageType.REQUEST)
                .senderId(String.valueOf(requestId))
                .userId(event.getInitiatorId())
                .build());

    }

    private void sendChangedRequestStatus(String userId, Long eventId, List<Long> requestId, String newStatus) {
        requestsSupplier.sendMessageToQueue(RequestStatusMqDto.builder()
                .userId(userId)
                .eventId(eventId)
                .request(requestId)
                .newStatus(newStatus)
                .build());
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(UpdateEventAdminRequest body, long eventId) {
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format(Constants.EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND)
        );

        updateData(
                event, body.getAnnotation(),
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
    public boolean isExistByInitiator(String userId) {
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
                .toList();
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
        final List<Predicate> predicateList = new ArrayList<>();

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
        return states.stream().map(EventState::from).toList();
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
    private Event getEventForUser(String userId, long eventId) {
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
        final EventStateAction newEventState = EventStateAction.from(body.getStateAction());
        if (newEventState == null) return;

        final Set<EventStateAction> availableStats = Set.of(EventStateAction.CANCEL_REVIEW, EventStateAction.SEND_TO_REVIEW);
        checkStatus(availableStats, newEventState);
        event.setState(newEventState.getEventState());
    }

    /**
     * Обновление статуса admin api
     */
    private void updateStatusByAdmin(UpdateEventAdminRequest body, Event event) {
        final EventStateAction newEventState = EventStateAction.from(body.getStateAction());
        if (newEventState == null) return;

        final Set<EventStateAction> availableStats = Set.of(EventStateAction.PUBLISH_EVENT, EventStateAction.REJECT_EVENT);
        checkStatus(availableStats, newEventState);

        final EventState currentEventState = event.getState();
        if (EventStateAction.PUBLISH_EVENT.equals(newEventState)) {
            throwIfNotAvailableStatus(Set.of(EventState.PUBLISHED, EventState.CANCELED), currentEventState, newEventState);
            event.setPublishedOn(LocalDateTime.now());
        } else if (EventStateAction.REJECT_EVENT.equals(newEventState)) {
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
                    set.stream().sorted().toList()));
        }
    }

    private void throwIfNotAvailableStatus(Set<EventState> set, EventState currentEventState, EventStateAction newEventState) {
        if (set.contains(currentEventState)) {
            throw new ConflictException(String.format(Constants.IMPOSSIBLE_S_WHEN_EVENT_STATUS_ONE_OF_S_CURRENT_STATUS_S,
                    newEventState, set.stream().sorted().toList(), currentEventState));
        }
    }

    private List<EventShortDto> getSortedList(List<EventShortDto> eventShortDtoList) {
        return eventShortDtoList.stream()
                .sorted(Comparator.comparing(EventShortDto::getViews))
                .toList();
    }
}
