package io.login.security.models;

import lombok.*;

import java.util.Optional;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest extends LoginUser {

    private Optional<String> resetPasswordToken;

}
