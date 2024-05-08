package com.jmca.easyauthapplication.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@ToString
@EqualsAndHashCode
public class ExceptionResponse {

    private final String message;

    @JsonIgnore
    private Date timestamp;

    public ExceptionResponse(String message, Date date) {
        this.message = message;
        this.timestamp = date;
    }
}
