package io.login.security.service;

import io.login.security.models.LoginRequest;
import io.login.security.models.LoginUser;

import javax.servlet.http.HttpServletResponse;

public interface IUserService {

    LoginUser createUser(LoginUser loginUser);
    LoginUser getLoginUser(String username);
    LoginUser resetPassword(LoginRequest loginRequest);
    LoginUser updateAccountStatus(LoginUser loginUser);
    void authenticate(LoginRequest loginRequest, HttpServletResponse response);
    String generateResetPasswordToken(LoginRequest loginRequest);
}
