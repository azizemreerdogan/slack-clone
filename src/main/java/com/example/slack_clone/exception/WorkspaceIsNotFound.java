package com.example.slack_clone.exception;

import org.springframework.http.HttpStatus;

public class WorkspaceIsNotFound extends BusinessExceptions{
    public WorkspaceIsNotFound(String message){
        super(message, HttpStatus.NOT_FOUND);
    }
}
