package io.login.server.controllers.open;

import io.jsonwebtoken.ExpiredJwtException;
import io.login.client.models.UserAccount;
import io.login.client.models.UserAuthContext;
import io.login.security.dao.IUserRepository;
import io.login.security.models.LoginRequest;
import io.login.security.models.LoginUser;
import io.login.security.service.IUserAuthenticationService;
import io.login.security.service.IUserService;
import io.login.security.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/open-login")
public class OpenAuthenticationApiController {

    private Logger LOGGER = LoggerFactory.getLogger(OpenAuthenticationApiController.class);
    private IUserService userService;

    private IUserRepository userRepository;

    @Autowired
    public void setUserRepository(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody LoginRequest loginRequest,
                                                    HttpServletResponse response){
        return this.userService.authenticate(loginRequest, response);
//        response.getWriter().write("resetPasswordToken"+loginRequest.getResetPasswordToken());
//        return ResponseEntity.ok(loginRequest);
    }

    @GetMapping("/validate-jwt/{jwt}")
    public ResponseEntity<UserAuthContext> validateJwt(@PathVariable String jwt) {
        LOGGER.info("validating the jwt");
        final String requestTokenHeader = jwt;

        String username = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                LOGGER.error("Unable to get JWT Token: {}",e.toString(),e);
                throw new RuntimeException(e);
            } catch (ExpiredJwtException e) {
                LOGGER.error("JWT Token has expired: {}",e.toString(),e);
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.warn("JWT Token does not begin with Bearer String");
        }

        if(username == null || username.length() == 0)
          throw new RuntimeException("username is empty or null");

        UserAccount loginUser = this.userService.getLoginUser(username);
        loginUser.setPassword("*****");
        loginUser.setRoles(this.userRepository.getRoles(username))  ;
//        loginUser.setUserProfile(); // fromDAO
        UserAuthContext userAuthContext = new UserAuthContext(loginUser,null);
        return ResponseEntity.ok(userAuthContext);
    }

    @PostMapping("/reset-system-password")
    public ResponseEntity<LoginUser> resetPassword(@RequestBody LoginRequest loginRequest) {
        LoginUser loginUser = this.userService.resetPassword(loginRequest);
        return ResponseEntity.ok(loginUser);
    }
}
