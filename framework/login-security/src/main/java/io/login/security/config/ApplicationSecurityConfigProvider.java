package io.login.security.config;

import io.login.client.config.security.SecurityConfigProvider;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSecurityConfigProvider extends SecurityConfigProvider {

    @Override
    protected String[] getIgnoredEndPoints() {
        return new String[]{
                "/open-login/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs"
        };
    }
}
