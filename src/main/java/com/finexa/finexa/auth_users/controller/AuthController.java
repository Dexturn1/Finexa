package com.finexa.finexa.auth_users.controller;


import com.finexa.finexa.auth_users.dtos.LoginRequest;
import com.finexa.finexa.auth_users.dtos.LoginResponse;
import com.finexa.finexa.auth_users.dtos.RegistrationRequest;
import com.finexa.finexa.auth_users.dtos.ResetPassWordRequest;
import com.finexa.finexa.auth_users.services.AuthService;
import com.finexa.finexa.res.Response;
import com.finexa.finexa.role.entity.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response<String>> createRole(@RequestBody @Valid RegistrationRequest registrationRequest){
        return ResponseEntity.ok(authService.register(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/forget-password")
    public ResponseEntity<Response<?>> forgetPassword (@RequestBody ResetPassWordRequest resetPassWordRequest){
        return ResponseEntity.ok(authService.forgetPassword(resetPassWordRequest.getEmail()));
    }

    @PostMapping("/rest-password")
    public ResponseEntity<Response<?>> restPassword (@RequestBody ResetPassWordRequest resetPassWordRequest){
        return ResponseEntity.ok(authService.updatePasswordViaResetCode(resetPassWordRequest));
    }


}
