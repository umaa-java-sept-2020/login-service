package io.login.security.dao;

import io.login.client.models.RoleUpdate;
import io.login.client.models.UserAccount;
import io.login.security.models.LoginUser;

public interface IUserRepository {

    void insertUserToDB(UserAccount userRequest);

    void insertIntoRoleMapping(UserAccount userRequest);

    RoleUpdate updateUSerRoleInDB(RoleUpdate updateRole);

    LoginUser getUserByUUID(String uuid);

    LoginUser getUserByUsername(String username);

    LoginUser updateUserPassword(LoginUser loginUser);

    LoginUser updateUserStatus(LoginUser loginUser);

    Integer getResetPasswordTokenCount(String username, String resetPasswordToken);

    boolean saveResetPasswordToken(String username, String resetPasswordToken);
}