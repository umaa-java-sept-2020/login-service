package io.login.security.service;

import io.login.security.models.LoginUser;

public interface IUserAuthenticationService {

    String authenticate(String username, String password) throws Exception;
    LoginUser getLoginUser(String username);
}
