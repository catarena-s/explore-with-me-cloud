package dev.shvetsova.ewmc.main.service.event;

import com.querydsl.core.types.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.shvetsova.ewmc.main.dto.event.EventFullDto;
import dev.shvetsova.ewmc.main.dto.event.EventShortDto;
import dev.shvetsova.ewmc.main.dto.event.NewEventDto;
import dev.shvetsova.ewmc.main.dto.event.UpdateEventAdminRequest;
import dev.shvetsova.ewmc.main.dto.event.UpdateEventUserRequest;
import dev.shvetsova.ewmc.main.dto.location.LocationDto;
import dev.shvetsova.ewmc.main.enums.EventState;
import dev.shvetsova.ewmc.main.enums.EventStateAction;
import dev.shvetsova.ewmc.main.enums.SortType;
import dev.shvetsova.ewmc.main.exception.ConflictException;
import dev.shvetsova.ewmc.main.exception.NotFoundException;
import dev.shvetsova.ewmc.main.exception.ValidateException;
import dev.shvetsova.ewmc.main.filter.EventFilter;
import dev.shvetsova.ewmc.main.filter.EventPredicate;
import dev.shvetsova.ewmc.main.mapper.EventMapper;
import dev.shvetsova.ewmc.main.model.Category;
import dev.shvetsova.ewmc.main.model.Event;
import dev.shvetsova.ewmc.main.model.Location;
import dev.shvetsova.ewmc.main.model.User;
import dev.shvetsova.ewmc.main.repository.EventRepository;
import dev.shvetsova.ewmc.main.service.category.CategoryService;
import dev.shvetsova.ewmc.main.service.location.LocationService;
import dev.shvetsova.ewmc.main.service.stats.StatsService;
import dev.shvetsova.ewmc.main.service.user.UserService;
import dev.shvetsova.ewmc.main.utils.Constants;
import dev.shvetsova.ewmc.main.utils.QPredicate;

//import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.shvetsova.ewmc.main.enums.EventState.*;
import static dev.shvetsova.ewmc.main.enums.EventStateAction.CANCEL_REVIEW;
import static dev.shvetsova.ewmc.main.enums.EventStateAction.PUBLISH_EVENT;
import static dev.shvetsova.ewmc.main.enums.EventStateAction.REJECT_EVENT;
import static dev.shvetsova.ewmc.main.enums.EventStateAction.SEND_TO_REVIEW;
import static dev.shvetsova.ewmc.main.utils.Constants.EVENT_WITH_ID_D_WAS_NOT_FOUND;
import static dev.shvetsova.ewmc.main.utils.Constants.START;
import static dev.shvetsova.ewmc.main.utils.Constants.THE_REQUIRED_OBJECT_WAS_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final String EVENT_DATE_AND_TIME_IS_BEFORE = "Event date and time cannot be earlier than %d hours from the";
    private final EventRepository eventRepository;

    private final UserService userService;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final StatsService statsService;

    @Override
    @Transactional
    public EventFullDto saveEvent(long userId, NewEventDto body) {
//        дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
        confirmEventDateIsAfterCurrent(body.getEventDate(), 2);
        final User user = userService.findUserById(userId);
        final Category category = categoryService.findCategoryById(body.getCategory());
        final Location location = locationService.findLocation(body.getLocation());

        final Event event = EventMapper.fromDto(body, user, category, location, PENDING, LocalDateTime.now());

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getEvents(long userId, int from, int size) {
        userService.checkExistById(userId);
        final PageRequest page = PageRequest.of(from / size, size);
        final List<Event> events = eventRepository.findAllByInitiatorId(userId, page).getContent();
        return events.stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(long userId, long eventId) {
        userService.checkExistById(userId);
        final Event event = getEventForUser(userId, eventId);
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
        final Event event = eventRepository.findByIdAndState(eventId, PUBLISHED)
                .orElseThrow(() -> new NotFoundException(
                        String.format(EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        THE_REQUIRED_OBJECT_WAS_NOT_FOUND));

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
        userService.checkExistById(userId);
        final Event event = getEventForUser(userId, eventId);

        if (event.getState().equals(PUBLISHED)) {
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
                        String.format(EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        THE_REQUIRED_OBJECT_WAS_NOT_FOUND));
    }

    @Override
    public List<Event> findEventsByIds(List<Long> eventIdList) {
        return eventRepository.findAllById(eventIdList);
    }

    private Map<String, Long> getViewStats(HttpServletRequest request, List<Event> events, boolean unique) {
        final LocalDateTime start = events.stream()
                .map(Event::getPublishedOn)
                .min(LocalDateTime::compareTo)
                .orElse(START);
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
                .stateEq(PUBLISHED)
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

    /** Получение списка статусов */
    private List<EventState> getEventStates(List<String> states) {
        return states.stream()
                .map(EventState::from)
                .collect(Collectors.toList());
    }

    /** Получить просмотры события */
    private static long getView(HttpServletRequest request, Map<String, Long> viewStats, Long eventId) {
        final String uri = request.getRequestURI() + "/" + eventId;
        return viewStats.getOrDefault(uri, 0L);
    }

    /** Получить событие пользователя */
    private Event getEventForUser(long userId, long eventId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format(EVENT_WITH_ID_D_WAS_NOT_FOUND, eventId),
                        THE_REQUIRED_OBJECT_WAS_NOT_FOUND));
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

    /** Обновление статуса для public api */
    private void updateStatusByUser(UpdateEventUserRequest body, Event event) {
        final EventStateAction newEventState = body.getStateAction();
        if (newEventState == null) return;

        final Set<EventStateAction> availableStats = Set.of(CANCEL_REVIEW, SEND_TO_REVIEW);
        checkStatus(availableStats, newEventState);
        event.setState(newEventState.getEventState());
    }

    /** Обновление статуса admin api */
    private void updateStatusByAdmin(UpdateEventAdminRequest body, Event event) {
        final EventStateAction newEventState = body.getStateAction();
        if (newEventState == null) return;

        final Set<EventStateAction> availableStats = Set.of(PUBLISH_EVENT, REJECT_EVENT);
        checkStatus(availableStats, newEventState);

        final EventState currentEventState = event.getState();
        if (PUBLISH_EVENT.equals(newEventState)) {
            throwIfNotAvailableStatus(Set.of(PUBLISHED, CANCELED), currentEventState, newEventState);
            event.setPublishedOn(LocalDateTime.now());
        } else if (REJECT_EVENT.equals(newEventState)) {
            throwIfNotAvailableStatus(Set.of(PUBLISHED, CANCELED), currentEventState, newEventState);
        }
        event.setState(newEventState.getEventState());
    }

    private void updateEventDate(LocalDateTime newEventDate, Event event, int difHour) {
        if (newEventDate != null) {
            confirmEventDateIsAfterCurrent(newEventDate, difHour);
            if (PUBLISHED.equals(event.getState())) {
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
