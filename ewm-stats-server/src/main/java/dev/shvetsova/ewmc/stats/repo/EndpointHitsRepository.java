package dev.shvetsova.ewmc.stats.repo;

import dev.shvetsova.ewmc.stats.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EndpointHitsRepository extends JpaRepository<EndpointHit, Long>, QuerydslPredicateExecutor<EndpointHit> {
}