package io.login.security.service;

import io.login.client.models.UserAccount;
import io.login.security.models.LoginRequest;
import io.login.security.models.LoginUser;

import javax.servlet.http.HttpServletResponse;

public interface IUserService {

    void addUserIntoDB(UserAccount userRequest);
    void userRoleMapping(UserAccount userRequest);
    LoginUser getLoginUser(String username);
    LoginUser resetPassword(LoginRequest loginRequest);
    LoginUser updateAccountStatus(LoginUser loginUser);
    void authenticate(LoginRequest loginRequest, HttpServletResponse response);
    String generateResetPasswordToken(LoginRequest loginRequest);
}
