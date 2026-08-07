package com.finexa.finexa.notification.dtos;


import com.finexa.finexa.auth_users.entity.User;
import com.finexa.finexa.enums.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {

    private Long id;
    private String subject;
    @NotBlank(message = "Recipient is required")
    private String recipient;
    private String body;
    private NotificationType type; // Email SMS PUSH


    private User user;

    private LocalDateTime createdAt;

    // for value / variables to be passed into email template to send
    private String templateName;
    private Map<String, Object> templateVariables;

}
