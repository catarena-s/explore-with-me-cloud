package dev.shvetsova.ewmc.dto.notification;

import dev.shvetsova.ewmc.enums.SenderType;

import java.io.Serializable;

public class NewNotificationDto implements Serializable {
    private Long userId;
    private Long senderId;
    private SenderType senderType;
    private String text;

    @Override
    public String toString() {
        return "NewNotificationDto{" +
                "userId=" + userId +
                ", senderId=" + senderId +
                ", messageType=" + senderType +
                ", text='" + text + '\'' +
                '}';
    }

    public NewNotificationDto() {
    }

    NewNotificationDto(Long userId, Long senderId, SenderType senderType, String text) {
        this.userId = userId;
        this.senderId = senderId;
        this.senderType = senderType;
        this.text = text;
    }

    public static NewNotificationDtoBuilder builder() {
        return new NewNotificationDtoBuilder();
    }

    public Long getUserId() {
        return this.userId;
    }

    public Long getSenderId() {
        return this.senderId;
    }

    public SenderType getMessageType() {
        return this.senderType;
    }

    public String getText() {
        return this.text;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setMessageType(SenderType senderType) {
        this.senderType = senderType;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static class NewNotificationDtoBuilder {
        private Long userId;
        private Long senderId;
        private SenderType senderType;
        private String text;

        NewNotificationDtoBuilder() {
        }

        public NewNotificationDtoBuilder consumerId(Long userId) {
            this.userId = userId;
            return this;
        }

        public NewNotificationDtoBuilder senderId(Long senderId) {
            this.senderId = senderId;
            return this;
        }

        public NewNotificationDtoBuilder messageType(SenderType senderType) {
            this.senderType = senderType;
            return this;
        }

        public NewNotificationDtoBuilder text(String text) {
            this.text = text;
            return this;
        }

        public NewNotificationDto build() {
            return new NewNotificationDto(this.userId, this.senderId, this.senderType, this.text);
        }
    }
}
