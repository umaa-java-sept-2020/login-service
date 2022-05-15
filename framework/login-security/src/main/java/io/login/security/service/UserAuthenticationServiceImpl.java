package io.login.security.service;

import io.login.client.models.UserAccount;
import io.login.client.models.UserAuthContext;
import io.login.security.models.LoginUser;
import io.login.security.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserAuthenticationServiceImpl implements IUserAuthenticationService {

    private AuthenticationManager authenticationManager;

    private UserDetailsService jwtUserDetailsService;

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    public void setJwtUserDetailsService(UserDetailsService jwtUserDetailsService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
            throw e;
        }
        return generateJwtToken(username);
    }

    protected String generateJwtToken(String username) {
        final UserDetails userDetails = jwtUserDetailsService
                .loadUserByUsername(username);
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername(userDetails.getUsername());
        final String token = jwtTokenUtil.generateToken(loginUser);

        return token;

    }

    @Override
    public UserAccount getLoginUser(String username)  {
        final UserDetails userDetails = jwtUserDetailsService
                .loadUserByUsername(username);
        UserAuthContext userAuthContext = (UserAuthContext) userDetails;
        UserAccount loginUser = userAuthContext.getUserRequest();
        return loginUser;
    }
}
