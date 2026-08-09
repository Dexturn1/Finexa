package com.finexa.finexa.auth_users.services.impl;


import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.finexa.finexa.auth_users.dtos.UpdatePasswordRequest;
import com.finexa.finexa.auth_users.dtos.UserDTO;
import com.finexa.finexa.auth_users.entity.PasswordResetCode;
import com.finexa.finexa.auth_users.entity.User;
import com.finexa.finexa.auth_users.repo.UserRepo;
import com.finexa.finexa.auth_users.services.AuthService;
import com.finexa.finexa.auth_users.services.UserService;
import com.finexa.finexa.exceptions.NotFoundException;
import com.finexa.finexa.notification.entity.Notification;
import com.finexa.finexa.notification.services.NotificationService;
import com.finexa.finexa.res.Response;
import jakarta.persistence.FetchType;
import jdk.javadoc.doclet.DocletEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {



    private final UserRepo userRepo;
    private final NotificationService notificationService;
    private final PasswordResetCode passwordResetCode;
    private final ModelMapper modelMapper;


    @Override
    public User getCurrentLoggedInUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            throw new NotFoundException("User is not authenticated");
        }

        String email = authentication.getName();
        return userRepo.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));
    }

    @Override
    public Response<UserDTO> getMyProfile() {
        User user = getCurrentLoggedInUser();
        UserDTO userDTO = modelMapper.map(user,UserDTO.class);

        return Response.<UserDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User retrieved")
                .build();
    }

    @Override
    public Response<Page<UserDTO>> getAllUsers(int page, int size) {
        Page<User> users = userRepo.findAll(PageRequest.of(page, size));

        Page<UserDTO> userDTOS = users.map(user ->
            modelMapper.map(user, UserDTO.class));

        return Response.<Page<UserDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User retrieved")
                .data(userDTOS)
                .build();

    }

    @Override
    public Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        return null;
    }

    @Override
    public Response<?> uploadProfilePicture(MultipartFile file) {
        return null;
    }
}
