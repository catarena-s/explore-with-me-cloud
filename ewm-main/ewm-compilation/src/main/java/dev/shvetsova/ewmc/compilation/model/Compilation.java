package dev.shvetsova.ewmc.compilation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Подборка событий
 */
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "compilation")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column
    private String title;
    @Column
    private Boolean pinned;

//    @OneToMany(mappedBy = "compilation", fetch = FetchType.LAZY)
//    private List<CompilationEvent> compilationEvents;
}
