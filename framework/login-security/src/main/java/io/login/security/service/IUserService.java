package io.login.security.service;

import io.login.security.models.LoginRequest;
import io.login.security.models.LoginUser;

import javax.servlet.http.HttpServletResponse;

public interface IUserService {

    LoginUser createUser(LoginUser loginUser);
    LoginUser resetPassword(LoginRequest loginRequest);
    LoginUser updateAccountStatus(LoginUser loginUser);
    LoginUser authenticate(LoginRequest loginRequest, HttpServletResponse response);
    LoginRequest generateResetPasswordToken(LoginRequest loginRequest);
}
