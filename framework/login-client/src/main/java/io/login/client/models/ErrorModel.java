package io.login.client.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorModel {

    private String errorMessage; // technical
    private String userInterfaceMessage; // non-technical
    private int httpStatusCode;
    private int applicationErrorCode;

}
