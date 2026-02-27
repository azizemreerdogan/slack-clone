package com.example.slack_clone.workspace.dto;

import java.util.UUID;

public record WorkspaceResponse(
        UUID id,
        String name,
        String description,
        String slug
) {}