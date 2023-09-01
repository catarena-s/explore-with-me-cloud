package dev.shvetsova.ewmc.compilation.repository;

import dev.shvetsova.ewmc.compilation.model.CompilationEvent;
import dev.shvetsova.ewmc.compilation.model.CompilationEventKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationEventRepository extends JpaRepository<CompilationEvent, CompilationEventKey> {
    @Query("select c.id.eventId from CompilationEvent c where c.id.compilationId=:compilation")
    List<Long> findAllByCompilationId(long compilation);

    void deleteAllById_CompilationId(long compId);
}
