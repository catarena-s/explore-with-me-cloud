package dev.shvetsova.ewmc.subscription.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "friendship")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private String followerId;

    @Column(name = "friend_id")
    private String friendId;

    @Enumerated(EnumType.STRING)
    private FriendshipState state;

    @Column(name = "created_on")
    private LocalDateTime createdOn;
}
