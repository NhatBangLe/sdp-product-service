package io.github.nhatbangle.sdp.product.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;
import java.util.StringJoiner;

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var errors = e.getFieldErrors();
        var joiner = new StringJoiner("\n");
        errors.forEach(error -> {
            var msg = String.format("%s %s.", error.getField(), error.getDefaultMessage());
            joiner.add(msg);
        });

        var message = joiner.toString();
        log.debug(message, e);
        return message;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidKeyException.class)
    public String handleInvalidKeyException(InvalidKeyException e) {
        log.debug(e.getLocalizedMessage(), e.getCause());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e) {
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
