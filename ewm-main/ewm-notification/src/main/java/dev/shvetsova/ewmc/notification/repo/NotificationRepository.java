package dev.shvetsova.ewmc.notification.repo;

import dev.shvetsova.ewmc.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByIdAndUserId(long id, long userId);

    List<Notification> findAllByUserId(long userId);

    void deleteByIdAndUserId(long id, long userId);

    void deleteByIdInAndUserId(List<Long> ids, long userId);
}
