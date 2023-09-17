package dev.shvetsova.ewmc.request.model;

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
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "requester_id")
    @Column(name = "requester_id")
    private long requesterId;

//    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "event_id")
    private long eventId;

    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "private")
    private boolean isPrivate;
}
