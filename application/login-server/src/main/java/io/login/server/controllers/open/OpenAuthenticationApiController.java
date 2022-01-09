package io.login.server.controllers.open;

import io.login.security.models.LoginRequest;
import io.login.security.models.LoginUser;
import io.login.security.service.IUserService;
import io.login.security.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/open-login")
public class OpenAuthenticationApiController {

    private IUserService userService;

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
    public ResponseEntity<LoginUser> authenticate(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginUser loginUser = this.userService.authenticate(loginRequest, response);
        if (loginUser instanceof LoginRequest) {
            return ResponseEntity.ok().body(loginUser);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate-jwt/{jwt}")
    public ResponseEntity<?> validateJwt(@PathVariable String jwt) {
        return null;
    }

    @PostMapping("/reset-system-password")
    public ResponseEntity<LoginUser> resetPassword(@RequestBody LoginRequest loginRequest) {
        LoginUser loginUser = this.userService.resetPassword(loginRequest);
        return ResponseEntity.ok(loginUser);
    }
}
