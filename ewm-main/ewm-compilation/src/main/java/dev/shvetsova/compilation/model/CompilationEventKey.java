package dev.shvetsova.compilation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CompilationEventKey implements Serializable {
    @Column(name = "compilation_id")
    private long compilationId;

    @Column(name = "event_id")
    private long eventId;
}
