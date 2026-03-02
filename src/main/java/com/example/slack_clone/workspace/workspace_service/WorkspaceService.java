package com.example.slack_clone.workspace.workspace_service;


import com.example.slack_clone.entity.enums.WorkspaceStatus;
import com.example.slack_clone.exception.WorkspaceIsNotFound;
import com.example.slack_clone.exception.WorkspaceNameIsUsedException;
import com.example.slack_clone.workspace.Workspace;
import com.example.slack_clone.workspace.dto.UpdateWorkspaceRequestDto;
import com.example.slack_clone.workspace.dto.WorkspaceResponseDto;
import com.example.slack_clone.workspace.workspace_repo.WorkspaceRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepo workspaceRepo;

    //Create a workspace
    @Transactional
    public WorkspaceResponseDto createWorkspace(String w_name, String w_description, String w_slug){
        var existingWorkspace = workspaceRepo.findByName(w_name).orElse(null);

        //If there is already a workspace with this name, an exception is thrown
        if(existingWorkspace != null){
            throw new WorkspaceNameIsUsedException("There is a workspace with this name");
        }

        Workspace workspace = Workspace.builder()
                .name(w_name)
                .description(w_description)
                .slug(w_slug)
                .build();

        Workspace saved = workspaceRepo.save(workspace);

        //Respond with WorkspaceResponse
        return new WorkspaceResponseDto(
                saved.getW_id(),
                saved.getName(),
                saved.getDescription(),
                saved.getSlug(),
                saved.getStatus());
    }


    //List all workspaces
    @Transactional
    public List<WorkspaceResponseDto> listWorkspaces(){
        return workspaceRepo.findAll()
                .stream()
                .map(workspace -> new WorkspaceResponseDto(
                        workspace.getW_id(),
                        workspace.getName(),
                        workspace.getDescription(),
                        workspace.getSlug(),
                        workspace.getStatus()
                ))
                .toList();
    }


    //Update a workspace name
    @Transactional
    public WorkspaceResponseDto updateWorkspaceName(UUID w_id, String name){
        var existingWorkspace = workspaceRepo.findById(w_id).orElse(null);
        if(existingWorkspace == null){
            throw new WorkspaceIsNotFound("Workspace ID is not found.");
        }
        var sameNameWorkspace = workspaceRepo.findByName(name).orElse(null);
        if(sameNameWorkspace != null){
            throw new WorkspaceNameIsUsedException("Workspace is already exists with that name");
        }

        existingWorkspace.setName(name);
        Workspace saved = workspaceRepo.save(existingWorkspace);
        return new WorkspaceResponseDto(
                saved.getW_id(),
                saved.getName(),
                saved.getDescription(),
                saved.getSlug(),
                saved.getStatus()
                );
    }

    //Update a Workspace
    @Transactional
    public WorkspaceResponseDto updateWorkspace(UUID w_id, UpdateWorkspaceRequestDto request){
        Workspace workspace = workspaceRepo.findById(w_id).orElseThrow();

        if(request.name() != null){
            workspace.setName(request.name());
        }

        if(request.description() != null){
            workspace.setDescription(request.description());
        }

        if(request.status() != null){
            workspace.setStatus(request.status());
        }

        return new WorkspaceResponseDto(
                workspace.getW_id(),
                workspace.getName(),
                workspace.getDescription(),
                workspace.getSlug(),
                workspace.getStatus()
        );

    }


    //List all ACTIVE workspaces
    @Transactional
    public List<WorkspaceResponseDto> listActiveWorkspaces(){
        return workspaceRepo.findByStatus(WorkspaceStatus.ACTIVE)
                .stream()
                .map(workspace -> new WorkspaceResponseDto(
                        workspace.getW_id(),
                        workspace.getName(),
                        workspace.getDescription(),
                        workspace.getSlug(),
                        workspace.getStatus()
                        ))
                .toList();
    }

    //Delete a Workspace
    @Transactional
    public WorkspaceResponseDto deleteWorkspace(UUID w_id){
        Workspace existingWorkspace = workspaceRepo.findById(w_id)
                .orElseThrow(() -> new WorkspaceIsNotFound("No workspace is found with this id"));

        workspaceRepo.delete(existingWorkspace);

        return new WorkspaceResponseDto(
                existingWorkspace.getW_id(),
                existingWorkspace.getName(),
                existingWorkspace.getDescription(),
                existingWorkspace.getSlug(),
                existingWorkspace.getStatus()
        );
    }

}
