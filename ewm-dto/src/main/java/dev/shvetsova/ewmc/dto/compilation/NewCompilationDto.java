
package dev.shvetsova.ewmc.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Подборка событий
 */
public class NewCompilationDto {
    @NotBlank(message = "Title cannot be null or empty")
    @Size(min = 1, max = 50, message = "size must be between 1 and 50")
    private String title;
    private boolean pinned = false;

    NewCompilationDto(@NotBlank(message = "Title cannot be null or empty")
                      @Size(min = 1, max = 50, message = "size must be between 1 and 50") String title,
                      boolean pinned,
                      List<Long> events) {
        this.title = title;
        this.pinned = pinned;
        this.events = events;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public void setEvents(List<Long> events) {
        this.events = events;
    }

    private List<Long> events;

    public String getTitle() {
        return title;
    }

    public boolean isPinned() {
        return pinned;
    }

    public List<Long> getEvents() {
        return events;
    }

    public static NewCompilationDtoBuilder builder() {
        return new NewCompilationDtoBuilder();
    }

    public static class NewCompilationDtoBuilder {
        @NotBlank(message = "Title cannot be null or empty")
        @Size(min = 1, max = 50, message = "size must be between 1 and 50")
        private String title;
        private boolean pinned;
        private List<Long> events;

        NewCompilationDtoBuilder() {
        }

        public NewCompilationDtoBuilder title(@NotBlank(message = "Title cannot be null or empty")
                                              @Size(min = 1, max = 50, message = "size must be between 1 and 50") String title) {
            this.title = title;
            return this;
        }

        public NewCompilationDtoBuilder pinned(boolean pinned) {
            this.pinned = pinned;
            return this;
        }

        public NewCompilationDtoBuilder events(List<Long> events) {
            this.events = events;
            return this;
        }

        public NewCompilationDto build() {
            return new NewCompilationDto(this.title, this.pinned, this.events);
        }
    }
}
