package com.example.slack_clone.workspace.dto;

import com.example.slack_clone.entity.enums.WorkspaceStatus;

import java.util.UUID;

public record WorkspaceResponseDto(
        UUID id,
        String name,
        String description,
        String slug,
        WorkspaceStatus status
) {}