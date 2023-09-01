package dev.shvetsova.ewmc.stats.service;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.shvetsova.ewmc.stats.dto.EndpointHitDto;
import dev.shvetsova.ewmc.stats.dto.EndpointHitMapper;
import dev.shvetsova.ewmc.stats.dto.ViewStatsDto;
import dev.shvetsova.ewmc.stats.exception.ValidateException;
import dev.shvetsova.ewmc.stats.model.EndpointHit;
import dev.shvetsova.ewmc.stats.model.EndpointHitFilter;
import dev.shvetsova.ewmc.stats.repo.EndpointHitsRepository;
import dev.shvetsova.ewmc.stats.utils.QPredicate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static dev.shvetsova.ewmc.stats.model.QEndpointHit.endpointHit;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final EndpointHitsRepository repository;
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void saveHit(EndpointHitDto dto) {
        final EndpointHit endpointHit = EndpointHitMapper.fromDto(dto);
        repository.save(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {

        if (start != null && end != null && start.isAfter(end)) {
            throw new ValidateException("Start time must be before end end");
        }
        final EndpointHitFilter filter = EndpointHitFilter.builder()
                .timestampAfter(start)
                .timestampBefore(end)
                .uris(uris)
                .build();

        return getByFilter(unique, filter);
    }

    private List<ViewStatsDto> getByFilter(Boolean unique, EndpointHitFilter filter) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
        final Predicate predicate = getEndpointHitPredicate(filter);
        final ConstructorExpression<ViewStatsDto> expression =
                Projections.constructor(
                        ViewStatsDto.class,
                        endpointHit.app,
                        endpointHit.uri,
                        Boolean.TRUE.equals(unique)
                                ? endpointHit.ip.countDistinct()
                                : endpointHit.count()
                );
        return queryFactory.from(endpointHit)
                .select(expression)
                .where(predicate)
                .groupBy(endpointHit.app, endpointHit.uri)
                .fetch();
    }

    private Predicate getEndpointHitPredicate(EndpointHitFilter filter) {
        return QPredicate.builder()
                .add(filter.getIp(), endpointHit.ip::eq)
                .add(filter.getApp(), endpointHit.app::eq)
                .add(filter.getUris(), endpointHit.uri::in)
                .add(filter.getTimestampAfter(), endpointHit.timestamp::after)
                .add(filter.getTimestampBefore(), endpointHit.timestamp::before)
                .buildAnd();
    }
}