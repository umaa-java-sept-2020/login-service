package io.login.security.service;

import io.login.client.models.RoleUpdate;
import io.login.client.models.UserAccount;
import io.login.client.models.UserStatus;
import io.login.security.dao.IUserRepository;
import io.login.security.models.LoginRequest;
import io.login.security.models.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public String authenticate(LoginRequest loginRequest, HttpServletResponse response) {
        LoginUser loginUser = this.userRepository.getUserByUsername(loginRequest.getUsername());
        if(loginUser.getStatus() == UserStatus.DRAFT){
          String token  =  generateResetPasswordToken(loginRequest);
          response.setHeader("resetPasswordToken", token);
////          response.getWriter().write("resetPasswordToken"+ token);
//            ResponseEntity.status(204).body("resetPasswordToken"+ token);
          return token;
        }

        if(loginUser.getStatus() == UserStatus.ACTIVE) {
            try {
                String jwtToken = userAuthenticationService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
                response.setHeader("Authorization", "Bearer " + jwtToken);
                // response.getWriter().write("Authorization"+ "Bearer " + jwtToken);
                return "Bearer "+jwtToken;
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

    @Override
    public void addUserIntoDB(UserAccount userRequest) {
        String uuid = UUID.randomUUID().toString();
        userRequest.setUuid(uuid);
        System.out.println("msg2");
        this.userRepository.insertUserToDB(userRequest);
    }

    @Override
    public void saveUserRoleMapping(UserAccount userRequest) {
        this.userRepository.insertIntoRoleMapping(userRequest);
    }

    @Override
    public void updateUserRole(RoleUpdate updateRole) {
        this.userRepository.updateUSerRoleInDB(updateRole);
    }

    @Override
    public LoginUser getLoginUser(String username) {
        return userAuthenticationService.getLoginUser(username);
    }
}
