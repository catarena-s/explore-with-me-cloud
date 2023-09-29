package dev.shvetsova.ewmc.notification.repo;

import dev.shvetsova.ewmc.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByIdAndConsumerId(long id, String userId);

    List<Notification> findAllByConsumerId(String userId);

    void deleteByIdAndConsumerId(long id, String userId);

    void deleteByIdInAndConsumerId(List<Long> ids, String userId);
}
