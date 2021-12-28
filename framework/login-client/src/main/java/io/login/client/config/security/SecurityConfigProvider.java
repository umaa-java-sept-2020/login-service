package io.login.client.config.security;

import io.login.client.config.LoginConfig;
import io.login.client.config.security.components.JwtAuthenticationEntryPoint;
import io.login.client.config.security.components.JwtRequestFilter;
import io.login.client.config.security.components.NoEncryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

public abstract class SecurityConfigProvider {

    private AuthenticationEntryPoint authenticationEntryPoint;

    protected AuthenticationEntryPoint getAuthenticationEntryPoint(){
        if(authenticationEntryPoint == null)
            authenticationEntryPoint = new JwtAuthenticationEntryPoint();
        return authenticationEntryPoint;
    }

    private PasswordEncoder passwordEncoder;
    protected PasswordEncoder getPasswordEncoder(){
        if(passwordEncoder == null){
            passwordEncoder = new NoEncryptPasswordEncoder();
        }
        return passwordEncoder;
    };

    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void setJwtRequestFilter(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    public JwtRequestFilter getJwtRequestFilter() {
        return jwtRequestFilter;
    }

    private UserDetailsService userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    protected abstract String[] getIgnoredEndPoints();


    private LoginConfig loginConfig;

    @Autowired
    public void setLoginConfig(LoginConfig loginConfig) {
        this.loginConfig = loginConfig;
    }

    public String getLoginUrl(){
        return loginConfig.getLoginUrl();
    }

}
