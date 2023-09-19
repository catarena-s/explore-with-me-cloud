package dev.shvetsova.ewmc.request.repository;

import dev.shvetsova.ewmc.request.model.Request;
import dev.shvetsova.ewmc.request.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByIdAndRequesterId(long requestId, String userId);

    List<Request> findAllByRequesterId(String userId);

//    List<Request> findAllByEvent_InitiatorIdAndEventId(long userId, long eventId);

    boolean existsByEventIdAndRequesterId(long eventId, String userId);

    boolean existsByRequesterId(String userId);

    @Query(value = "select r from Request r where r.id in (:requestIds) and r.status = :name")
    List<Request> findAllByIdInAndStatus(List<Long> requestIds, RequestStatus name);

    List<Request> findAllByEventId(long eventId);

    @Query("select r.eventId from Request r where r.requesterId in(:friendsId)")
    List<Long> findAllEventsForRequesters(List<Long> friendsId);
}
