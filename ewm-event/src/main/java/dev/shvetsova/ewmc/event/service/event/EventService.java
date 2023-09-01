package dev.shvetsova.ewmc.event.service.event;

import dev.shvetsova.ewmc.event.dto.event.*;
import dev.shvetsova.ewmc.event.model.enums.SortType;
import dev.shvetsova.ewmc.event.model.Event;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    /**
     * @param userId
     * @param body
     * @return
     */
    EventFullDto saveEvent(long userId, NewEventDto body);

    /**
     * @param eventId
     * @return
     */

    EventFullDto getEventById(long eventId);

    EventFullDto updateEventByUser(UpdateEventUserRequest body, long userId, long eventId);

    List<EventShortDto> getPublishedEvents(long userId, int from, int size);

    /**
     * Поиск событий
     *
     * @param users      список id пользователей, чьи события нужно найти
     * @param states     список состояний в которых находятся искомые события
     * @param categories список id категорий в которых будет вестись поиск
     * @param rangeStart дата и время не раньше которых должно произойти событие
     * @param rangeEnd   дата и время не позже которых должно произойти событие
     * @param from       количество событий, которые нужно пропустить для формирования текущего набора default: 0
     * @param size       количество событий в наборе default: 10
     * @return возвращает полную информацию обо всех событиях подходящих под переданные условия
     * - если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     */
    List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    /**
     * @param body
     * @param eventId
     * @return
     */
    EventFullDto updateEventByAdmin(UpdateEventAdminRequest body, long eventId);

    /**
     * Получение событий с возможностью фильтрации
     *
     * @param text          текст для поиска в содержимом аннотации и подробном описании события
     * @param categories    список идентификаторов категорий в которых будет вестись поиск
     * @param paid          поиск только платных/бесплатных событий
     * @param rangeStart    дата и время не раньше которых должно произойти событие
     * @param rangeEnd      дата и время не позже которых должно произойти событие
     * @param onlyAvailable только события у которых не исчерпан лимит запросов на участие
     * @param sort          Вариант сортировки: по дате события или по количеству просмотров
     * @param from          количество событий, которые нужно пропустить для формирования текущего набора default: 0
     * @param size          количество событий в наборе
     * @param request
     * @return если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     */
    List<EventShortDto> getPublishedEvents(String text, List<Long> categories, Boolean paid,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                           Boolean onlyAvailable, SortType sort,
                                           Integer from, Integer size, HttpServletRequest request);


    /**
     * Получение подробной информации об опубликованном событии по его идентификатору
     * - событие должно быть опубликовано <br>
     * - информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов <br>
     * - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики <br>
     *
     * @param id      id события
     * @param request
     * @return В случае, если события с заданным id не найдено, возвращает статус код 404 <br>
     */
    EventFullDto getPublishedEvent(long id, HttpServletRequest request);

    Event findEventById(long eventId);

    List<EventShortDto> findEventsByIds(List<Long> eventIdList);

    boolean isExistByInitiator(long userId);

    List<EventShortDto> getPublishedEvents(long userId, List<Long> friendsId);

    List<EventShortDto> getParticipateEventList(long userId, List<Long> friendsId);

    void upConfirmedRequests(long userId, long eventId);
}
