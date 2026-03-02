package com.example.slack_clone.exception;

import org.springframework.http.HttpStatus;

public class WorkspaceNameIsUsedException extends BusinessExceptions{
    public WorkspaceNameIsUsedException(String message){
        super(message, HttpStatus.CONFLICT);
    }
}
