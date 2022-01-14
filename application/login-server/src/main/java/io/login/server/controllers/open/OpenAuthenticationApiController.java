package io.login.server.controllers.open;

import io.jsonwebtoken.ExpiredJwtException;
import io.login.client.models.UserAuthContext;
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

@RestController
@RequestMapping("/open-login")
public class OpenAuthenticationApiController {

    private Logger LOGGER = LoggerFactory.getLogger(OpenAuthenticationApiController.class);
    private IUserService userService;

    private IUserAuthenticationService userAuthenticationService;

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public void setUserAuthenticationService(IUserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginUser> authenticate(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        this.userService.authenticate(loginRequest, response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/validate-jwt/{jwt}")
    public ResponseEntity<?> validateJwt(@PathVariable String jwt) {
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

        LoginUser loginUser = this.userAuthenticationService.getLoginUser(username);
        UserAuthContext userAuthContext = new UserAuthContext(loginUser);
        return ResponseEntity.ok(userAuthContext);
    }

    @PostMapping("/reset-system-password")
    public ResponseEntity<LoginUser> resetPassword(@RequestBody LoginRequest loginRequest) {
        LoginUser loginUser = this.userService.resetPassword(loginRequest);
        return ResponseEntity.ok(loginUser);
    }
}
