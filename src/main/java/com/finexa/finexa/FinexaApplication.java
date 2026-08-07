package com.finexa.finexa;

import com.finexa.finexa.auth_users.entity.User;
import com.finexa.finexa.enums.NotificationType;
import com.finexa.finexa.notification.dtos.NotificationDTO;
import com.finexa.finexa.notification.entity.Notification;
import com.finexa.finexa.notification.services.NotificationService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;



@RequiredArgsConstructor
@EnableAsync
@SpringBootApplication
public class FinexaApplication {


	private final NotificationService notificationService;

	public static void main(String[] args) {
		SpringApplication.run(FinexaApplication.class, args);
	}



//	@Bean
//	CommandLineRunner runner(){
//		return args -> {
//			NotificationDTO notificationDTO = NotificationDTO.builder()
//					.recipient("prabhatkapkoti1@gmailcom")
//					.subject("Hello testing Email")
//					.body("Hey this is a test email🙄")
//					.type(NotificationType.EMAIL)
//					.build();
//
//			notificationService.sendEmail(notificationDTO, new User());
//		};
//	}

}
