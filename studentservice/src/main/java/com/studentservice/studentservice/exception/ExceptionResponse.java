package com.studentservice.studentservice.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@EqualsAndHashCode
public class ExceptionResponse {


    private final String message;

    @JsonIgnore
    private Date timestamp;

    public ExceptionResponse(
            String message, Date timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
