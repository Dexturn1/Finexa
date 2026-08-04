package com.finexa.finexa.notification.repo;

import com.finexa.finexa.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
}
