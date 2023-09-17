package dev.shvetsova.ewmc.event.model;

import dev.shvetsova.ewmc.event.enums.EventState;
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
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column
    private String title;
    @Column
    private String annotation;//Краткое описание
    @Column
    private String description;//Полное описание события
    @Column
    private Boolean paid;//Нужно ли оплачивать участие

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "initiator_id")
    private long initiatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    //Ограничение на количество участников.
    // Значение 0 - означает отсутствие ограничения
    @Column(name = "participant_limit")
    private Integer participantLimit;

    //Количество одобренных заявок на участие в данном событии
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    //Дата и время создания события (в формате &quot;yyyy-MM-dd HH:mm:ss
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    //Дата и время публикации события (в формате &quot;yyyy-MM-dd HH:mm:ss)
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    // Нужна ли пре-модерация заявок на участие
    @Column(name = "request_moderation")
    private Boolean requestModeration;

    //Список состояний жизненного цикла события
    @Enumerated(EnumType.STRING)
    private EventState state;
}
