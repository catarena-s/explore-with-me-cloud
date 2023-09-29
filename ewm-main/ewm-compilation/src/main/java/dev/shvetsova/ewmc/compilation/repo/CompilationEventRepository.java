package dev.shvetsova.ewmc.compilation.repo;

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

    @Query("select new dev.shvetsova.ewmc.compilation.model.CompilationEventKey(c.id.compilationId,c.id.eventId) " +
            "from CompilationEvent c where c.id.compilationId in(:ids)")
    List<CompilationEventKey> findAllByCompilationIdIn(List<Long> ids);
}
