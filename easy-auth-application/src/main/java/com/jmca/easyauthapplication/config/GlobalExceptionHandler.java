package com.jmca.easyauthapplication.config;

import com.jmca.easyauthapplication.payload.response.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle invalid method arguments (validation errors)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        ExceptionResponse exceptionResponse = new ExceptionResponse("Validation Failed", errors, new Date());

        logger.error("Validation error: {}", errors);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGlobalExceptions(Exception ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), new Date());

        logger.error("General error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle Resource Not Found exceptions
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFound(NoHandlerFoundException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse("Resource not found", new Date());

        logger.error("Resource not found: {}", ex.getRequestURL());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    // Handle Access Denied exceptions
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedExceptions(AccessDeniedException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse("Access denied", new Date());

        logger.warn("Access denied: {}", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    // Handle IllegalArgumentException (common in invalid arguments or null)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentExceptions(IllegalArgumentException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), new Date());

        logger.error("Illegal argument: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
