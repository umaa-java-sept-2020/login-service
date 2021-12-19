package io.login.client.models;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserProfile {
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String city;
    // can add more details
}
