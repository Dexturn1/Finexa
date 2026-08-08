package com.finexa.finexa.auth_users.services;


import com.finexa.finexa.auth_users.dtos.LoginRequest;
import com.finexa.finexa.auth_users.dtos.LoginResponse;
import com.finexa.finexa.auth_users.dtos.RegistrationRequest;
import com.finexa.finexa.auth_users.dtos.ResetPassWordRequest;
import com.finexa.finexa.res.Response;

public interface AuthService {

    Response<String> register(RegistrationRequest registrationRequest);

    Response<LoginResponse> login(LoginRequest loginRequest);

    Response<String> forgetPassword(String email);

    Response<String> updatePasswordViaResetCode(ResetPassWordRequest resetPassWordRequest);


}
