package io.login.client.config.security.components;

import io.login.client.config.security.SecurityConfigProvider;
import io.login.client.models.UserAuthContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
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

    private RestTemplate restTemplate=new RestTemplate();

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
        String path = "/open-login/validate-jwt/";
        String url = securityConfigProvider.getLoginUrl() + path + requestTokenHeader;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            LOGGER.info("do rest call to the endpoint: {} for validating the jwt token", url);
            UserAuthContext userAuthContext = restTemplate.exchange(url, HttpMethod.GET,null, UserAuthContext.class).getBody();
            UserDetails userDetails = userAuthContext;
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userAuthContext, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            LOGGER.info("security context initialized");
        }
        chain.doFilter(request, response);
    }

}