package io.login.security.dao;

public interface IUserRepository {

    Object getUserByUUID(String uuid);
    Object getUserByUserId(String userId);
}
