package dev.shvetsova.ewmc.utils;

import dev.shvetsova.ewmc.exception.ValidateException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectUtilsTest {

    @Test
    void createPageRequest_ShouldCreateCorrectPageRequest() {
        // Given
        Integer from = 10;
        Integer size = 5;

        // When
        PageRequest pageRequest = ProjectUtils.createPageRequest(from, size);

        // Then
        assertNotNull(pageRequest);
        assertEquals(2, pageRequest.getPageNumber()); // 10 / 5 = 2
        assertEquals(5, pageRequest.getPageSize());
    }

    @Test
    void createPageRequest_WithZeroFrom_ShouldCreateFirstPage() {
        // Given
        Integer from = 0;
        Integer size = 10;

        // When
        PageRequest pageRequest = ProjectUtils.createPageRequest(from, size);

        // Then
        assertNotNull(pageRequest);
        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());
    }

    @Test
    void isNullOrEmpty_WithNullCollection_ShouldReturnTrue() {
        // When
        boolean result = ProjectUtils.isNullOrEmpty(null);

        // Then
        assertTrue(result);
    }

    @Test
    void isNullOrEmpty_WithEmptyCollection_ShouldReturnTrue() {
        // Given
        List<String> emptyList = Collections.emptyList();

        // When
        boolean result = ProjectUtils.isNullOrEmpty(emptyList);

        // Then
        assertTrue(result);
    }

    @Test
    void isNullOrEmpty_WithNonEmptyCollection_ShouldReturnFalse() {
        // Given
        List<String> list = List.of("item1", "item2");

        // When
        boolean result = ProjectUtils.isNullOrEmpty(list);

        // Then
        assertFalse(result);
    }

    @Test
    void validateDateRange_WithValidRange_ShouldNotThrowException() {
        // Given
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 12, 0);

        // When & Then
        assertDoesNotThrow(() -> ProjectUtils.validateDateRange(start, end));
    }

    @Test
    void validateDateRange_WithStartAfterEnd_ShouldThrowValidateException() {
        // Given
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 10, 0);

        // When & Then
        ValidateException exception = assertThrows(ValidateException.class,
                () -> ProjectUtils.validateDateRange(start, end));
        assertEquals("'rangeStart' must be before 'rangeEnd'", exception.getMessage());
    }

    @Test
    void validateDateRange_WithNullStart_ShouldNotThrowException() {
        // Given
        LocalDateTime start = null;
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 12, 0);

        // When & Then
        assertDoesNotThrow(() -> ProjectUtils.validateDateRange(start, end));
    }

    @Test
    void validateDateRange_WithNullEnd_ShouldNotThrowException() {
        // Given
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime end = null;

        // When & Then
        assertDoesNotThrow(() -> ProjectUtils.validateDateRange(start, end));
    }

    @Test
    void validateDateRange_WithBothNull_ShouldNotThrowException() {
        // Given
        LocalDateTime start = null;
        LocalDateTime end = null;

        // When & Then
        assertDoesNotThrow(() -> ProjectUtils.validateDateRange(start, end));
    }
}
