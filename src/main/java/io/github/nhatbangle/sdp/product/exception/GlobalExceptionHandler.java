package io.github.nhatbangle.sdp.product.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        log.debug(e.getLocalizedMessage(), e.getCause());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException e) {
        log.debug(e.getLocalizedMessage(), e.getCause());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidKeyException.class)
    public String handleInvalidKeyException(InvalidKeyException e) {
        log.debug(e.getLocalizedMessage(), e.getCause());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataConflictException.class)
    public String handleDataConflictException(DataConflictException e) {
        log.warn(e.getLocalizedMessage(), e.getCause());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ServiceUnavailableException.class)
    public String handleServiceUnavailableException(ServiceUnavailableException e) {
        log.warn(e.getLocalizedMessage(), e.getCause());
        return e.getMessage();
    }

}
