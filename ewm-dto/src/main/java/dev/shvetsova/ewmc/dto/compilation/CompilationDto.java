package dev.shvetsova.ewmc.dto.compilation;

import dev.shvetsova.ewmc.dto.event.EventShortDto;

import java.util.List;

/**
 * Подборка событий
 */
public class CompilationDto {
    private Long id;
    private Boolean pinned;
    private String title;
    private List<EventShortDto> events;

    public CompilationDto(Long id, Boolean pinned, String title, List<EventShortDto> events) {
        this.id = id;
        this.pinned = pinned;
        this.title = title;
        this.events = events;
    }

    public CompilationDto() {
    }

    public static CompilationDtoBuilder builder() {
        return new CompilationDtoBuilder();
    }

    public CompilationDtoBuilder toBuilder() {
        return new CompilationDtoBuilder().id(this.id).pinned(this.pinned).title(this.title).events(this.events);
    }

    public Long getId() {
        return id;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public String getTitle() {
        return title;
    }

    public List<EventShortDto> getEvents() {
        return events;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEvents(List<EventShortDto> events) {
        this.events = events;
    }

    public static class CompilationDtoBuilder {
        private Long id;
        private Boolean pinned;
        private String title;
        private List<EventShortDto> events;

        CompilationDtoBuilder() {
        }

        public CompilationDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CompilationDtoBuilder pinned(Boolean pinned) {
            this.pinned = pinned;
            return this;
        }

        public CompilationDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        public CompilationDtoBuilder events(List<EventShortDto> events) {
            this.events = events;
            return this;
        }

        public CompilationDto build() {
            return new CompilationDto(this.id, this.pinned, this.title, this.events);
        }

        public String toString() {
            return "CompilationDto.CompilationDtoBuilder(id=" + this.id + ", pinned=" + this.pinned + ", title=" + this.title + ", events=" + this.events + ")";
        }
    }
}
