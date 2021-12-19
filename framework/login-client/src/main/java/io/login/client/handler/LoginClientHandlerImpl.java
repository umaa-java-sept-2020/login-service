package io.login.client.handler;

import io.login.client.models.LoginRequest;
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
        String url = "";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setToken(Optional.of(token));

        return getUserAuthContext(url, loginRequest);

    }

    @Override
    public UserAuthContext authenticate(LoginRequest loginRequest) {
        loginRequest.setToken(Optional.empty());
        String url = "";
        return getUserAuthContext(url, loginRequest);
    }

    protected UserAuthContext getUserAuthContext(String url, LoginRequest loginRequest) {

        HttpEntity httpEntity = new HttpEntity(loginRequest);
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<UserAuthContext>() {
        }).getBody();
    }
}
