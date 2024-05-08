package com.jmca.easyauthapplication.config;

import com.jmca.easyauthapplication.payload.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ExceptionResponse> handleInvalidExceptions(
            MethodArgumentNotValidException methodArgumentNotValidException) {
        HashMap<String, String> errors = new HashMap<>();
        methodArgumentNotValidException
                .getBindingResult()
                .getAllErrors()
                .forEach(
                        error -> {
                            String fieldName = ((FieldError) error).getField();
                            String errorMessage = error.getDefaultMessage();
                            errors.put(fieldName, errorMessage);
                        });
        ExceptionResponse exceptionResponse = new ExceptionResponse(errors.toString(), new Date());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleValidationErrors(Exception ex) {
        return new ResponseEntity<>(
                new ExceptionResponse(ex.getMessage(), new Date()), HttpStatus.BAD_REQUEST);
    }
}
