package io.login.security.service;

import io.login.security.models.LoginRequest;
import io.login.security.models.LoginUser;

public interface IUserService {

    LoginUser createUser(LoginUser loginUser);
    LoginUser resetPassword(LoginRequest loginRequest);
    LoginUser updateAccountStatus(LoginUser loginUser);
    LoginUser authenticate(LoginRequest loginRequest);
    LoginRequest generateResetPasswordToken(LoginRequest loginRequest);
}
