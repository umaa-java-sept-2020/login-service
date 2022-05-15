package io.login.client.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginAppException extends RuntimeException {
    private ErrorModel errorModel;
    private Throwable throwable;

    // when actual exception occurs, that means e is not null
    public LoginAppException(ErrorModel errorModel, Throwable throwable) {
        super(throwable.getMessage());
        this.errorModel = errorModel;
        this.throwable = throwable;
        this.errorModel.setErrorMessage(throwable.getMessage());
    }

    public LoginAppException(ErrorModel errorModel) {
        this.errorModel = errorModel;
    }
}
