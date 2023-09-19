package dev.shvetsova.ewmc.notification.service;

import dev.shvetsova.ewmc.dto.notification.NewNotificationDto;
import dev.shvetsova.ewmc.dto.notification.NotificationDto;
import dev.shvetsova.ewmc.notification.mapper.NotificationMapper;
import dev.shvetsova.ewmc.notification.model.Notification;
import dev.shvetsova.ewmc.notification.repo.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void addNotification(NewNotificationDto dto) {
        Notification notification = NotificationMapper.fromDto(dto);
        notificationRepository.save(notification);
    }

    @Override
    public NotificationDto getById(String userId, long id) {
        Notification notification = notificationRepository.findByIdAndUserId(id, userId);
        return NotificationMapper.toDto(notification);
    }

    @Override
    public List<NotificationDto> getAllMsg(String userId) {
        List<Notification> notificationList = notificationRepository.findAllByUserId(userId);
        return notificationList.stream().map(NotificationMapper::toDto).toList();
    }

    @Override
    @Transactional
    public NotificationDto markAsRead(String userId, long id) {
        Notification notification = notificationRepository.findByIdAndUserId(id, userId);
        notification.setRead(true);
        notificationRepository.save(notification);
        return NotificationMapper.toDto(notification);
    }

    @Override
    @Transactional
    public List<NotificationDto> markAsReadList(String userId, List<Long> ids) {
        List<Notification> notificationList = notificationRepository.findAllByUserId(userId);
        notificationList.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notificationList);
        return notificationList.stream().map(NotificationMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void deleteById(String userId, long id) {
        notificationRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    @Transactional
    public void deleteList(String userId, List<Long> ids) {
        notificationRepository.deleteByIdInAndUserId(ids, userId);
    }
}
