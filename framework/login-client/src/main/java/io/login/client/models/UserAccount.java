package io.login.client.models;


import lombok.*;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserAccount {

    private String uuid;
    private String username; // can be email or x12345
    private String password;
    private List<UserRole> roles;
    private UserStatus status;
    private UserProfile userProfile;
}
