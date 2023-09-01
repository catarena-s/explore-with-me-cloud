package dev.shvetsova.ewmc.main.repository;

import dev.shvetsova.ewmc.main.model.Request;
import dev.shvetsova.ewmc.common.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByIdAndRequesterId(long requestId, long userId);

    List<Request> findAllByRequesterId(long userId);

//    List<Request> findAllByIdInAndStatus(List<Long> requestIds, RequestStatus requestStatus);

    List<Request> findAllByEvent_InitiatorIdAndEventId(long userId, long eventId);

    boolean existsByEventIdAndRequesterId(long eventId, long userId);

    boolean existsByRequesterId(long userId);

    @Query(value = "select r from Request r where r.id in (:requestIds) and r.status = :name")
    List<Request> findAllByIdInAndStatus(List<Long> requestIds, RequestStatus name);
}
