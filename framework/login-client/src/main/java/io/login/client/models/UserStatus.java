package io.login.client.models;


import lombok.*;

@Getter
@ToString
public enum UserStatus {
    ACTIVE, INACTIVE, DRAFT, SUSPENDED;
}
