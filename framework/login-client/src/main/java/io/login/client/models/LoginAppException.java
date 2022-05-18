package io.login.client.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginAppException extends RuntimeException {
    private ErrorModel errorModel;
    private Throwable throwable;

    // when actual exception occurs, that means throwable is not null
    public LoginAppException(ErrorModel errorModel, Throwable throwable) {
        super(throwable.getMessage());
        this.errorModel = errorModel;
        this.throwable = throwable;
        // setting global error message, any exception occures the controller will move to
        // LoginAppException class and set the respective fields of ErrorModel Class
        this.errorModel.setErrorMessage(throwable.getMessage());
    }

    public LoginAppException(ErrorModel errorModel) {
        this.errorModel = errorModel;
    }
}
