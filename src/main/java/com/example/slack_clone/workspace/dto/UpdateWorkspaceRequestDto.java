package com.example.slack_clone.workspace.dto;
import com.example.slack_clone.entity.enums.WorkspaceStatus;
import lombok.Getter;



public record UpdateWorkspaceRequestDto (
        String name,
        String description,
        String slug,
        WorkspaceStatus status
) {}
