
package dev.shvetsova.ewmc.dto.compilation;

import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Изменение информации о подборке событий. Если поле в запросе не указано (равно null) - значит изменение этих данных не треубется.
 */
public class UpdateCompilationRequest {
    @Size(min = 1, max = 50, message = "size must be between 1 and 50")
    private String title;
    private Boolean pinned;
    private List<Long> events;

    UpdateCompilationRequest(@Size(min = 1, max = 50, message = "size must be between 1 and 50") String title, Boolean pinned, List<Long> events) {
        this.title = title;
        this.pinned = pinned;
        this.events = events;
    }

    public static UpdateCompilationRequestBuilder builder() {
        return new UpdateCompilationRequestBuilder();
    }

    public @Size(min = 1, max = 50, message = "size must be between 1 and 50") String getTitle() {
        return this.title;
    }

    public Boolean getPinned() {
        return this.pinned;
    }

    public List<Long> getEvents() {
        return this.events;
    }

    public void setTitle(@Size(min = 1, max = 50, message = "size must be between 1 and 50") String title) {
        this.title = title;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    public void setEvents(List<Long> events) {
        this.events = events;
    }

    public UpdateCompilationRequestBuilder toBuilder() {
        return new UpdateCompilationRequestBuilder().title(this.title).pinned(this.pinned).events(this.events);
    }

    public static class UpdateCompilationRequestBuilder {
        private @Size(min = 1, max = 50, message = "size must be between 1 and 50") String title;
        private Boolean pinned;
        private List<Long> events;

        UpdateCompilationRequestBuilder() {
        }

        public UpdateCompilationRequestBuilder title(@Size(min = 1, max = 50, message = "size must be between 1 and 50") String title) {
            this.title = title;
            return this;
        }

        public UpdateCompilationRequestBuilder pinned(Boolean pinned) {
            this.pinned = pinned;
            return this;
        }

        public UpdateCompilationRequestBuilder events(List<Long> events) {
            this.events = events;
            return this;
        }

        public UpdateCompilationRequest build() {
            return new UpdateCompilationRequest(this.title, this.pinned, this.events);
        }
    }
}
