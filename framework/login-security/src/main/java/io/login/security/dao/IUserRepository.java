package io.login.security.dao;

import io.login.security.models.LoginUser;

public interface IUserRepository {

    LoginUser getUserByUUID(String uuid);
    LoginUser getUserByUsername(String username);
    LoginUser updateUserPassword(LoginUser loginUser);
    LoginUser updateUserStatus(LoginUser loginUser);
    Integer getResetPasswordTokenCount(String username, String resetPasswordToken);
    boolean saveResetPasswordToken(String username, String resetPasswordToken);
}
