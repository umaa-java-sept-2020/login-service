package io.login.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class LoginConfig {

    @Value("${login.url}")
    private String loginUrl;

    private String[] authenticatedPaths;
}
