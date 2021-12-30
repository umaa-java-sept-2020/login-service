package io.login.security.service;

import io.login.client.models.UserStatus;
import io.login.security.dao.IUserRepository;
import io.login.security.models.LoginRequest;
import io.login.security.models.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService{

    private IUserRepository userRepository;

    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public LoginUser createUser(LoginUser loginUser) {
        return null;
    }

    @Override
    public LoginUser resetPassword(LoginRequest loginRequest) {
        if(1==this.userRepository.getResetPasswordTokenCount(loginRequest.getUsername(), loginRequest.getResetPasswordToken().get())) {
            LoginUser loginUser = this.userRepository.getUserByUsername(loginRequest.getUsername());
            loginUser.setPassword(loginUser.getPassword());
            this.userRepository.updateUserPassword(loginUser);
            loginUser.setUserStatus(UserStatus.ACTIVE);
            this.updateAccountStatus(loginUser);
            return loginUser;
        }

        throw new RuntimeException("reset password token is not valid for the user");
    }

    @Override
    public LoginUser updateAccountStatus(LoginUser loginUser) {
        return this.userRepository.updateUserStatus(loginUser);
    }

    @Override
    public LoginUser authenticate(LoginRequest loginRequest) {
        LoginUser loginUser = this.userRepository.getUserByUsername(loginRequest.getUsername());
        if(loginUser.getUserStatus() == UserStatus.DRAFT){
          loginRequest =  generateResetPasswordToken(loginRequest);
          return loginRequest;
        }

        if(loginUser.getUserStatus() == UserStatus.ACTIVE)
            return loginUser;

        throw new RuntimeException("operation not allowed. user status is: "+loginUser.getUserStatus().name());
    }

    @Override
    public LoginRequest generateResetPasswordToken(LoginRequest loginRequest) {
        String resetPasswordToken = UUID.randomUUID().toString();
        boolean flag =this.userRepository.saveResetPasswordToken(loginRequest.getUsername(), resetPasswordToken);
        if(!flag)
            throw new RuntimeException("error while persisting reset password token");
        loginRequest.setResetPasswordToken(Optional.of(resetPasswordToken));
        return loginRequest;
    }
}
