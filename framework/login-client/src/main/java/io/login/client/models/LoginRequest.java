package io.login.client.models;

import lombok.*;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest {
    private String username;
    private String password;
    private Optional<String> token;
}
