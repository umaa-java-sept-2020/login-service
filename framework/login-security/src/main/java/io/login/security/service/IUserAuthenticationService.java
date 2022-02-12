package io.login.security.service;

import io.login.client.models.UserAccount;
import io.login.security.models.LoginUser;

public interface IUserAuthenticationService {

    String authenticate(String username, String password) throws Exception;
    UserAccount getLoginUser(String username);
}
