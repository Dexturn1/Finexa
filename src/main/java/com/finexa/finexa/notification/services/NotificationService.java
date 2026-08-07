package com.finexa.finexa.notification.services;

import com.finexa.finexa.auth_users.entity.User;
import com.finexa.finexa.notification.dtos.NotificationDTO;
import com.finexa.finexa.notification.entity.Notification;

import java.net.UnknownServiceException;

public interface NotificationService {

    void sendEmail(NotificationDTO notificationDTO, User user);
}
