package io.login.security.dao;

import io.login.client.models.*;
import io.login.security.models.LoginUser;

import java.util.List;

public interface IUserRepository {

    void insertUser(UserAccount userRequest);

    List<UserRole> getRoles(String userName);

    void insertIntoRoleMapping(UserAccount userRequest);

    RoleUpdate updateUSerRoleInDB(RoleUpdate updateRole);

    void insertUserProfileDetails(UserProfile userProfile);

    void updateUserProfileDetails(UserProfile userProfile);

    LoginUser getUserByUUID(String uuid);

    LoginUser getUserByUsername(String username);

    LoginUser updateUserPassword(LoginUser loginUser);

    LoginUser updateUserStatus(LoginUser loginUser);

    Integer getResetPasswordTokenCount(String username, String resetPasswordToken);

    boolean saveResetPasswordToken(String username, String resetPasswordToken);
}