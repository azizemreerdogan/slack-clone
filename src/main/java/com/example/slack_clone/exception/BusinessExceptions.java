package com.example.slack_clone.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BusinessExceptions extends RuntimeException {
    HttpStatus status;

    public BusinessExceptions(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
