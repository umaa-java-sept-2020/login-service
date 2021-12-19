package io.login.client.models;


import lombok.*;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequest {

    private String uuid;
    private String accountId; // can be email or x12345
    private String password;
    private UserRole role;
    private UserStatus status;
    private Optional<UserProfile> userProfile;
}
