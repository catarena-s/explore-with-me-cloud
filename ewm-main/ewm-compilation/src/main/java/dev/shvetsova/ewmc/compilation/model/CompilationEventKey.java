package dev.shvetsova.ewmc.compilation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
//@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompilationEventKey implements Serializable {
    @Column(name = "compilation_id")
    private long compilationId;

    @Column(name = "event_id")
    private long eventId;

    public CompilationEventKey(long compilationId, long eventId) {
        this.compilationId = compilationId;
        this.eventId = eventId;
    }
}
