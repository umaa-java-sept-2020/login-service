package io.login.security.service;

import io.login.client.models.RoleUpdate;

import io.login.client.models.UserAccount;
import io.login.client.models.UserProfile;
import io.login.security.models.LoginRequest;
import io.login.security.models.LoginUser;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IUserService {

    UserAccount getLoginUser(String username);
    void createUser(UserAccount userRequest);
    void saveUserRoleMapping(UserAccount userRequest);
    void updateUserRole(RoleUpdate updateRole);
    void createUserProfileDetails(UserProfile userProfile);
    void updateUserProfileDetails(UserProfile userProfile);
    LoginUser resetPassword(LoginRequest loginRequest);
    LoginUser updateAccountStatus(LoginUser loginUser);
    String authenticate(LoginRequest loginRequest, HttpServletResponse response);
    String generateResetPasswordToken(LoginRequest loginRequest);
    List<UserAccount> getUserRole();
}
