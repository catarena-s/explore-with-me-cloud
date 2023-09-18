package dev.shvetsova.ewmc.notification.model;

import dev.shvetsova.ewmc.enums.MessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "sender_id")
    private long senderId;

    private boolean read;

    private LocalDateTime created;
    @Column(name = "sender_type")
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
}
