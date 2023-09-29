package dev.shvetsova.ewmc.dto.notification;

import java.time.LocalDateTime;

public class NotificationDto {
    private long id;
    private String userId;
    private String text;
    private boolean idRead;
    private LocalDateTime created;

    NotificationDto(long id, String userId, String text, boolean idRead, LocalDateTime created) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.idRead = idRead;
        this.created = created;
    }

    public static NotificationDtoBuilder builder() {
        return new NotificationDtoBuilder();
    }

    public long getId() {
        return this.id;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getText() {
        return this.text;
    }

    public boolean isIdRead() {
        return this.idRead;
    }

    public LocalDateTime getCreated() {
        return this.created;
    }

    public static class NotificationDtoBuilder {
        private long id;
        private String userId;
        private String text;
        private boolean idRead;
        private LocalDateTime created;

        NotificationDtoBuilder() {
        }

        public NotificationDtoBuilder id(long id) {
            this.id = id;
            return this;
        }

        public NotificationDtoBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public NotificationDtoBuilder text(String text) {
            this.text = text;
            return this;
        }

        public NotificationDtoBuilder idRead(boolean idRead) {
            this.idRead = idRead;
            return this;
        }

        public NotificationDtoBuilder created(LocalDateTime created) {
            this.created = created;
            return this;
        }

        public NotificationDto build() {
            return new NotificationDto(this.id, this.userId, this.text, this.idRead, this.created);
        }
    }
}
