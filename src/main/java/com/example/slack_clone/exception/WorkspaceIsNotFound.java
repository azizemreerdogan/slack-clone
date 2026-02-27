package com.example.slack_clone.exception;

public class WorkspaceIsNotFound extends RuntimeException{
    public WorkspaceIsNotFound(String message){
        super(message);
    }
}
