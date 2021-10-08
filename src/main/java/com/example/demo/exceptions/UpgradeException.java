package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UpgradeException extends RuntimeException{
    public UpgradeException() {
    }

    public UpgradeException(String message) {
        super(message);
    }
}
