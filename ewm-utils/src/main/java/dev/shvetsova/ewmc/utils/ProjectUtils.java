package dev.shvetsova.ewmc.utils;

import dev.shvetsova.ewmc.exception.ValidateException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Утилитный класс с общими методами для работы с проектом
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProjectUtils {

    /**
     * Создаёт PageRequest для пагинации
     *
     * @param from начальный индекс (с 0)
     * @param size размер страницы
     * @return PageRequest
     */
    public static PageRequest createPageRequest(Integer from, Integer size) {
        return PageRequest.of(from / size, size);
    }

    /**
     * Проверяет, является ли коллекция null или пустой
     *
     * @param collection коллекция для проверки
     * @return true, если коллекция null или пустая
     */
    public static boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Валидирует диапазон дат: start должен быть до end
     *
     * @param rangeStart начальная дата
     * @param rangeEnd   конечная дата
     * @throws ValidateException если start после end
     */
    public static void validateDateRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidateException("'rangeStart' must be before 'rangeEnd'");
        }
    }
}
