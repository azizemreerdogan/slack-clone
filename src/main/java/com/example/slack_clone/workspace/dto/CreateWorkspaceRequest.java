package com.example.slack_clone.workspace.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateWorkspaceRequest(
        @NotBlank String name,
        String description,
        @NotBlank String slug
) {}