package com.finexa.finexa.auth_users.services.impl;

import com.finexa.finexa.account.entity.Account;
import com.finexa.finexa.auth_users.dtos.LoginRequest;
import com.finexa.finexa.auth_users.dtos.LoginResponse;
import com.finexa.finexa.auth_users.dtos.RegistrationRequest;
import com.finexa.finexa.auth_users.dtos.ResetPassWordRequest;
import com.finexa.finexa.auth_users.entity.User;
import com.finexa.finexa.auth_users.repo.UserRepo;
import com.finexa.finexa.auth_users.services.AuthService;
import com.finexa.finexa.enums.AccountType;
import com.finexa.finexa.enums.Currency;
import com.finexa.finexa.exceptions.BadRequestException;
import com.finexa.finexa.exceptions.NotFoundException;
import com.finexa.finexa.notification.dtos.NotificationDTO;
import com.finexa.finexa.notification.services.NotificationService;
import com.finexa.finexa.res.Response;
import com.finexa.finexa.role.entity.Role;
import com.finexa.finexa.role.repo.RoleRepo;
import com.finexa.finexa.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {


    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final NotificationService notificationService;



    @Override
    public Response<String> register(RegistrationRequest request) {

        List<Role> roles;

        if(request.getRoles() == null || request.getRoles().isEmpty()){
            // DEFAULT  TO CUSTOMER
            Role defaultRole = roleRepo.findByName("CUSTOMER")
                    .orElseThrow(()-> new NotFoundException("CUSTOMER ROLE NOT FOUND"));
            roles = Collections.singletonList(defaultRole);
        }else{
            roles = request.getRoles().stream()
                    .map(roleName-> roleRepo.findByName(roleName)
                            .orElseThrow(()-> new NotFoundException("ROLE NOT FOUND" + roleName)))
                    .collect(Collectors.toList());
        }

        if(userRepo.findByEmail(request.getEmail()).isPresent()){
            throw new BadRequestException(("Email Already Present"));
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .active(true)
                .build();

        User savedUser = userRepo.save(user);

        //TODO AUTO GENERATE AN ACCOUNT NUMBERT FOR THE USER
        Account savedAccount = accountService.createAccount(AccountType.SAVINGS, savedUser);

        //SEND WELCOME EMAIL
        Map<String, Object> vars = new HashMap<>();
        vars.put("name", savedUser.getFirstName());


        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(savedUser.getEmail())
                .subject("Welcome to Finexa 🎉")
                .templateName("welcome")
                .templateVariables(vars)
                .build();


        notificationService.sendEmail(notificationDTO, savedUser);

        // SEND ACCOUNT CREATION/DEATILS EMAIL
        Map<String, Object> accountVars = new HashMap<>();
        accountVars.put("name", savedUser.getFirstName());
        accountVars.put("accountNumber", savedAccount.getAccountNumber());
        accountVars.put("AccountType", AccountType.SAVINGS.name());
        accountVars.put("currency", Currency.USD);


        NotificationDTO accountCreatedEmail  = NotificationDTO.builder()
                .recipient(savedUser.getEmail())
                .subject("Your New Bank Account Has Been Created ✅")
                .templateName("account-created")
                .templateVariables(vars)
                .build();

        notificationService.sendEmail(accountCreatedEmail, savedUser);
        return Response.<String> builder()
                .statusCode(HttpStatus.OK.value())
                .message("Your account has been created successfully")
                .data("Email of your details has been sent to you. Your account number is: " + savedAccount.getAccountNumber())
                .build();

    }

    @Override
    public Response<LoginResponse> login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Email Not Found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Password doesn't match");
        }

        String token = tokenService.generateToken(user.getEmail());

        LoginResponse loginResponse = LoginResponse.builder()
                .roles(user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .toList())
                .token(token)
                .build();

        return Response.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Login Successful")
                .data(loginResponse)
                .build();

    }

    @Override
    public Response<String> forgetPassword(String email) {
        return null;
    }

    @Override
    public Response<String> updatePasswordViaResetCode(ResetPassWordRequest resetPassWordRequest) {
        return null;
    }
}
