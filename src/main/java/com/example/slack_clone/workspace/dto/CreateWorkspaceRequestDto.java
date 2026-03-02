package com.example.slack_clone.workspace.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateWorkspaceRequestDto(
        @NotBlank String name,
        String description,
        @NotBlank String slug
) {}