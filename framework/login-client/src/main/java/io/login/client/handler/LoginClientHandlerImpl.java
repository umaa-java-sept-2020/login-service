package io.login.client.handler;

import io.login.client.models.UserAuthContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class LoginClientHandlerImpl implements ILoginHandler {

    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
    }

    @Override
    public UserAuthContext validateToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        return null;

    }
}
