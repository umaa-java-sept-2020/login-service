package io.login.client.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginConfig {

    private String loginUrl;

    private String[] authenticatedPaths;
}
