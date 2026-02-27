package com.example.slack_clone.exception;

public class WorkspaceNameIsUsedException extends RuntimeException{
    public WorkspaceNameIsUsedException(String message){
        super(message);
    }
}
