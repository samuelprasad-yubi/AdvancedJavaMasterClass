package com.studentservice.studentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ExceptionResponse> handleInvalidExceptions(MethodArgumentNotValidException methodArgumentNotValidException) {
        HashMap<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ExceptionResponse exceptionResponse = new ExceptionResponse(String.join(", ", errors.values()), new Date());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception exception) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("error ", exception.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public final ResponseEntity<Object> handleUnauthorizedExceptions(Exception exception) {
        return new ResponseEntity<>("Not Authorized", HttpStatus.UNAUTHORIZED);
    }
}


