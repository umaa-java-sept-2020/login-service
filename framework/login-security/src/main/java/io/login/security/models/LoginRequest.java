package io.login.security.models;

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
    private Optional<String> resetPasswordToken;

    public Optional<String> getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(Optional<String> resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }
}
