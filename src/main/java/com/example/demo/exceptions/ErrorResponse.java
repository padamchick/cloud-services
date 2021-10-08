package com.example.demo.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
public class ErrorResponse {
    private final Instant timestamp;
    private final String message;
    private final HttpStatus status;
    private final int code;

    public ErrorResponse(String message, HttpStatus status, int code) {
        this.message = message;
        this.status = status;
        this.code = code;
        this.timestamp = Instant.now();
    }
}
