package io.login.security.service;

public interface IUserAuthenticationService {

    String authenticate(String username, String password) throws Exception;
}
