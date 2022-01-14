package io.login.security.service;

import io.login.client.models.UserStatus;
import io.login.security.dao.IUserRepository;
import io.login.security.models.LoginRequest;
import io.login.security.models.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService{

    private IUserRepository userRepository;

    private IUserAuthenticationService userAuthenticationService;

    @Autowired
    public void setUserAuthenticationService(IUserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public LoginUser createUser(LoginUser loginUser) {
        return null;
    }

    @Override
    @Transactional
    public LoginUser resetPassword(LoginRequest loginRequest) {
        if(1==this.userRepository.getResetPasswordTokenCount(loginRequest.getUsername(), loginRequest.getResetPasswordToken().get())) {
            LoginUser loginUser = this.userRepository.getUserByUsername(loginRequest.getUsername());
            loginUser.setPassword(loginRequest.getPassword());
            this.userRepository.updateUserPassword(loginUser);// update
            loginUser.setStatus(UserStatus.ACTIVE);
            this.updateAccountStatus(loginUser); // database
            return loginUser;
        }

        throw new RuntimeException("reset password token is not valid for the user");
    }

    @Override
    public LoginUser updateAccountStatus(LoginUser loginUser) {
        return this.userRepository.updateUserStatus(loginUser);
    }

    @Override
    public void authenticate(LoginRequest loginRequest, HttpServletResponse response) {
        LoginUser loginUser = this.userRepository.getUserByUsername(loginRequest.getUsername());
        if(loginUser.getStatus() == UserStatus.DRAFT){
          String token  =  generateResetPasswordToken(loginRequest);
          response.setHeader("resetPasswordToken", token);
          return;
        }

        if(loginUser.getStatus() == UserStatus.ACTIVE) {
            try {
                String jwtToken = userAuthenticationService.authenticate(loginUser.getUsername(), loginUser.getPassword());
                response.setHeader("Authorization", "Bearer " + jwtToken);
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("operation not allowed. user status is: "+loginUser.getStatus().name());
    }

    @Override
    public String generateResetPasswordToken(LoginRequest loginRequest) {
        String resetPasswordToken = UUID.randomUUID().toString();
        boolean flag =this.userRepository.saveResetPasswordToken(loginRequest.getUsername(), resetPasswordToken);
        if(!flag)
            throw new RuntimeException("error while persisting reset password token");
        return resetPasswordToken;
    }
}
