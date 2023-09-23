package dev.shvetsova.ewmc.dto.notification;

import dev.shvetsova.ewmc.enums.MessageType;

public class NewNotificationDto {
    private String userId;
    private String senderId;
    private MessageType messageType;
    private String text;

    NewNotificationDto(String userId, String senderId, MessageType messageType, String text) {
        this.userId = userId;
        this.senderId = senderId;
        this.messageType = messageType;
        this.text = text;
    }

    public static NewNotificationDtoBuilder builder() {
        return new NewNotificationDtoBuilder();
    }

    public String getUserId() {
        return this.userId;
    }

    public String getSenderId() {
        return this.senderId;
    }

    public MessageType getMessageType() {
        return this.messageType;
    }

    public String getText() {
        return this.text;
    }

    public static class NewNotificationDtoBuilder {
        private String userId;
        private String senderId;
        private MessageType messageType;
        private String text;

        NewNotificationDtoBuilder() {
        }

        public NewNotificationDtoBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public NewNotificationDtoBuilder senderId(String senderId) {
            this.senderId = senderId;
            return this;
        }

        public NewNotificationDtoBuilder messageType(MessageType messageType) {
            this.messageType = messageType;
            return this;
        }

        public NewNotificationDtoBuilder text(String text) {
            this.text = text;
            return this;
        }

        public NewNotificationDto build() {
            return new NewNotificationDto(this.userId, this.senderId, this.messageType, this.text);
        }

        public String toString() {
            return "NewNotificationDto.NewNotificationDtoBuilder(userId=" + this.userId + ", senderId=" + this.senderId + ", messageType=" + this.messageType + ", text=" + this.text + ")";
        }
    }
}
