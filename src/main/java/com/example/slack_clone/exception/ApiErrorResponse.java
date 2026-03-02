package com.example.slack_clone.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse (
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path
) {}