package com.interview.template.configs;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.interview.template.exceptions.InvalidUserNameException;
import com.interview.template.exceptions.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@EnableWebMvc
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFound(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorDetails(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUserNameException.class)
    public final ResponseEntity<Object> handleInvalidUsername(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorDetails(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberFormatException.class)
    public final ResponseEntity<Object> handleNumberFormatException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorDetails(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<Object> handleIllegalArgumentException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorDetails(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({TransactionSystemException.class, SQLIntegrityConstraintViolationException.class})
    public final ResponseEntity<Object> handleConstraintVoilationExceptions(Exception ex, WebRequest request) {

        Throwable cause = ((TransactionSystemException) ex).getRootCause();
        List<String> errors = new ArrayList<>();
        if (cause instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) cause)
                    .getConstraintViolations();
            constraintViolations.forEach(constraintViolation -> errors.add(constraintViolation.getMessage()));
        }
        String error = String.join(", ", errors);
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new ErrorDetails(HttpStatus.BAD_REQUEST, error), HttpStatus.BAD_REQUEST);
    }

    //TODO Handle other than duplicate key value unique constraint

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<Object> handleDataIntegrityExceptions(DataIntegrityViolationException ex, WebRequest request) {

        String specificCauseMessage = ex.getMostSpecificCause().getMessage();
        Pattern duplicatePattern = Pattern.compile("(?:Detail: Key.*\\()(.*)(?:)(?:deleted_token)(?:.*\\)=\\()(.*)(?:\\,.*\\))(.*)");
        Matcher matcher = duplicatePattern.matcher(specificCauseMessage);
        if (matcher.find()) {
            return new ResponseEntity<>(new ErrorDetails(HttpStatus.NOT_ACCEPTABLE, matcher.group(1) + " " + matcher.group(2) + " " + matcher.group(3)),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ErrorDetails(HttpStatus.NOT_ACCEPTABLE, ex.getRootCause().getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public final ResponseEntity<Object> handleHttpServerErrorException(HttpServerErrorException ex,
                                                                       WebRequest request) {
        log.error(ex.getMessage(), ex);
        HttpStatus statusCode = ex.getStatusCode();
        switch (statusCode) {
            case NOT_FOUND:
                return new ResponseEntity<>(
                        new ErrorDetails("invalid_resource", ex.getRawStatusCode(), "Resource not found"),
                        ex.getStatusCode());
            case INTERNAL_SERVER_ERROR:
                return new ResponseEntity<>(new ErrorDetails("internal_server_error", ex.getRawStatusCode(),
                        "Internal server error"), ex.getStatusCode());
            default:
                return new ResponseEntity<>(new ErrorDetails(ex.getStatusCode(), ex.getLocalizedMessage()),
                        ex.getStatusCode());
        }
    }

}
