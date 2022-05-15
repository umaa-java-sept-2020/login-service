package io.login.server.advice;

import io.login.client.models.ErrorModel;
import io.login.client.models.LoginAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppErrorAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppErrorAdvice.class);

    @ExceptionHandler(value = {LoginAppException.class})
    public ResponseEntity<ErrorModel> handleLoginAppException(LoginAppException ex) {
        ErrorModel errorModel = ex.getErrorModel();
        int httpStatusCode = errorModel.getHttpStatusCode() == 0 ? 500 : errorModel.getHttpStatusCode();
        /**
         * {@link httpStatusCode } must have valid value from here {@link org.springframework.http.HttpStatus }
         */
        errorModel.setHttpStatusCode(httpStatusCode);
        if (ex.getThrowable() != null) {
            LOGGER.error("error occurred: {}", ex.getThrowable());
        }
        return ResponseEntity.status(httpStatusCode).body(errorModel);
    }

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<ErrorModel> handleUnknownException(Throwable ex) {
        ErrorModel errorModel = new ErrorModel();
        errorModel.setHttpStatusCode(500);
        errorModel.setErrorMessage(ex.getMessage());
        errorModel.setApplicationErrorCode(12123); // using random value now. it may have significance later
        errorModel.setUserInterfaceMessage("Unknown error occurred");
        int httpStatusCode = errorModel.getHttpStatusCode() == 0 ? 500 : errorModel.getHttpStatusCode();
        /**
         * {@link httpStatusCode } must have valid value from here {@link org.springframework.http.HttpStatus }
         */
        errorModel.setHttpStatusCode(httpStatusCode);
        if (ex != null) {
            LOGGER.error("error occurred: {}", ex);
        }
        return ResponseEntity.status(httpStatusCode).body(errorModel);
    }
}
