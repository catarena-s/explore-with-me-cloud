package dev.shvetsova.ewmc.compilation.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "compilation_events")
public class CompilationEvent {
    @EmbeddedId
    private CompilationEventKey id;

//    @ManyToOne
////    @MapsId("id")
//    @JoinColumn(name = "compilation_id")
//    Compilation compilationId;

    //    @ManyToOne
//    @MapsId("event_id")
//    @JoinColumn
//    @Column(name = "event_id")
//    Long eventId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "compilation_id")
//    private Compilation compilation;
//
//    @Column(name = "event_id")
//    private Long eventId;
}
