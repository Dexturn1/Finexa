package com.finexa.finexa.auth_users.services;

import org.springframework.data.domain.Page;
import com.finexa.finexa.auth_users.dtos.ResetPassWordRequest;
import com.finexa.finexa.auth_users.dtos.UpdatePasswordRequest;
import com.finexa.finexa.auth_users.dtos.UserDTO;
import com.finexa.finexa.auth_users.entity.User;
import com.finexa.finexa.res.Response;
import org.springframework.web.multipart.MultipartFile;

import javax.print.DocFlavor;

public interface UserService {

    User getCurrentLoggedInUser();

    Response<UserDTO> getMyProfile();

    Response<Page<UserDTO>> getAllUsers(int page, int size);

    Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest);

    Response<?> uploadProfilePicture(MultipartFile file);


}
