package io.login.client.handler;

import io.login.client.models.LoginRequest;
import io.login.client.models.UserAuthContext;

import javax.servlet.http.HttpServletRequest;

public interface ILoginHandler {

    /**
     * http calls to login server for validating the token
     * @param request
     * @return
     */
    UserAuthContext validateToken(HttpServletRequest request);

    /**
     * http calls to login server for authentication
     * @param loginRequest
     * @return
     */
    UserAuthContext authenticate(LoginRequest loginRequest);
}
