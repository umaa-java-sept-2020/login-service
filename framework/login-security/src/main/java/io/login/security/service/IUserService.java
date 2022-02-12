package io.login.security.service;

import io.login.client.models.RoleUpdate;

import io.login.client.models.UserAccount;
import io.login.security.models.LoginRequest;
import io.login.security.models.LoginUser;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IUserService {

    UserAccount getLoginUser(String username);
    void addUserIntoDB(UserAccount userRequest);
    void saveUserRoleMapping(UserAccount userRequest);
    void updateUserRole(RoleUpdate updateRole);
    LoginUser resetPassword(LoginRequest loginRequest);
    LoginUser updateAccountStatus(LoginUser loginUser);
    String authenticate(LoginRequest loginRequest, HttpServletResponse response);
    String generateResetPasswordToken(LoginRequest loginRequest);
}
