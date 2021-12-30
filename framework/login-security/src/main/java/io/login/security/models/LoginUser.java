package io.login.security.models;

import io.login.client.models.UserRole;
import io.login.client.models.UserStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginUser {
    private String uuid;
    private String username;
    private String password;
    private UserStatus userStatus;
    private List<UserRole> userRoles;
 }
