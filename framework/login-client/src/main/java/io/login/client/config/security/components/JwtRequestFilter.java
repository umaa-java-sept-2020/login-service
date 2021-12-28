package io.login.client.config.security.components;

import io.login.client.config.security.SecurityConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter implements ApplicationContextAware {


    private Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);

    private SecurityConfigProvider securityConfigProvider;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        if (securityConfigProvider == null)
            securityConfigProvider = applicationContext.getBean(SecurityConfigProvider.class);

        final String requestTokenHeader = request.getHeader("Authorization");
        // do rest call
        String path = "/open-login/validate-jwt/jwt";
        LOGGER.info("do rest call to the endpoint: {} for validating the jwt token", securityConfigProvider.getLoginUrl() + path);
        chain.doFilter(request, response);
    }

}