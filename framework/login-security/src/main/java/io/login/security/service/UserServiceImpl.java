package io.login.security.service;

import io.login.client.models.*;

import io.login.security.dao.IUserRepository;
import io.login.security.models.LoginRequest;
import io.login.security.models.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

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
        if (1 == this.userRepository.getResetPasswordTokenCount(loginRequest.getUsername(), loginRequest.getResetPasswordToken().get())) {
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
        if (loginUser.getStatus() == UserStatus.DRAFT) {
            String token = generateResetPasswordToken(loginRequest);
            response.setHeader("resetPasswordToken", token);
////          response.getWriter().write("resetPasswordToken"+ token);
//            ResponseEntity.status(204).body("resetPasswordToken"+ token);
            return token;
        }

        if (loginUser.getStatus() == UserStatus.ACTIVE) {
            try {
                String jwtToken = userAuthenticationService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
                response.setHeader("Authorization", "Bearer " + jwtToken);
                // response.getWriter().write("Authorization"+ "Bearer " + jwtToken);
                return "Bearer " + jwtToken;
            } catch (BadCredentialsException e) {
                ErrorModel errorModel = new ErrorModel();
                errorModel.setHttpStatusCode(401);
                errorModel.setApplicationErrorCode(12341);
                errorModel.setUserInterfaceMessage("username or password incorrect");
                throw new LoginAppException(errorModel, e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("operation not allowed. user status is: " + loginUser.getStatus().name());
    }

    @Override
    public String generateResetPasswordToken(LoginRequest loginRequest) {
        String resetPasswordToken = UUID.randomUUID().toString();
        boolean flag = this.userRepository.saveResetPasswordToken(loginRequest.getUsername(), resetPasswordToken);
        if (!flag)
            throw new RuntimeException("error while persisting reset password token");
        return resetPasswordToken;
    }

    @Override
    public List<UserAccount> getUserRole() {
        return null;
    }

    @Override
    public UserAccount getLoginUser(String username) {
        return userAuthenticationService.getLoginUser(username);
    }

    public void createUser(UserAccount userRequest) {
        String uuid = UUID.randomUUID().toString();
        userRequest.setUuid(uuid);
        try {
            this.userRepository.insertUser(userRequest);
        } catch (DuplicateKeyException e) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.setApplicationErrorCode(12341);
            errorModel.setUserInterfaceMessage("duplicate username requested");
            throw new LoginAppException(errorModel, e);
        }
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
    public void createUserProfileDetails(UserProfile userProfile) {
        this.userRepository.insertUserProfileDetails(userProfile);
    }

    @Override
    public void updateUserProfileDetails(UserProfile userProfile) {
        this.userRepository.updateUserProfileDetails(userProfile);
    }
}
