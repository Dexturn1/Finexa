package com.finexa.finexa.auth_users.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;


@Data
public class UpdatePasswordRequest {

    @NotBlank(message = "Old Password is required")
    private String oldPassword;

    @NotBlank(message = "New Password is required")
    private String newPassword;




}
