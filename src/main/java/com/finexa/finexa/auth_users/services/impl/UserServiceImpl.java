package com.finexa.finexa.auth_users.services.impl;


import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.finexa.finexa.auth_users.dtos.UpdatePasswordRequest;
import com.finexa.finexa.auth_users.dtos.UserDTO;
import com.finexa.finexa.auth_users.entity.PasswordResetCode;
import com.finexa.finexa.auth_users.entity.User;
import com.finexa.finexa.auth_users.repo.UserRepo;
import com.finexa.finexa.auth_users.services.AuthService;
import com.finexa.finexa.auth_users.services.UserService;
import com.finexa.finexa.exceptions.BadRequestException;
import com.finexa.finexa.exceptions.NotFoundException;
import com.finexa.finexa.notification.dtos.NotificationDTO;
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
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {



    private final UserRepo userRepo;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;


    private final String uploadDir = "uploads/profile-pictures/";


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
        User user = getCurrentLoggedInUser();


        String newPassword = updatePasswordRequest.getNewPassword();
        String oldPassword = updatePasswordRequest.getOldPassword();

        if(oldPassword == null || newPassword == null){
            throw new BadRequestException("Old and new Password Required");
        }

        //validate the old passwor
        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new BadRequestException("Old Password not Correct");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());

        userRepo.save(user);

        // Send password change confirmation email.
        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("name", user.getFirstName());


        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(user.getEmail())
                .subject("Your Password was successfully changed")
                .templateName("password-change")
                .templateVariables(templateVariables)
                .build();


        notificationService.sendEmail(notificationDTO, user);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Password Change Successfully")
                .build();

    }

    @Override
    public Response<?> uploadProfilePicture(MultipartFile file) {
        User user = getCurrentLoggedInUser();


        try {
            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }


            if(user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()){
                Path oldFile = Paths.get(user.getProfilePictureUrl());
                if(Files.exists(oldFile)){
                    Files.delete(oldFile);
                }
            }

            //Generate a unique file name to avoid conficts
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            if(originalFileName != null && originalFileName.contains(".")){
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            String newFileName = UUID.randomUUID() + fileExtension;
            Path filePath = uploadPath.resolve(newFileName);

            Files.copy(file.getInputStream(), filePath);

            String fileUrl = filePath.toString();
            user.setProfilePictureUrl(fileUrl);
            userRepo.save(user);

            return Response.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Profile Picture uploaded successfully")
                    .data(fileUrl)
                    .build();


        }
        catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }

    }
}
