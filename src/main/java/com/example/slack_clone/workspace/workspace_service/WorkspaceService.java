package com.example.slack_clone.workspace.workspace_service;


import com.example.slack_clone.exception.WorkspaceIsNotFound;
import com.example.slack_clone.exception.WorkspaceNameIsUsedException;
import com.example.slack_clone.workspace.Workspace;
import com.example.slack_clone.workspace.dto.WorkspaceResponse;
import com.example.slack_clone.workspace.workspace_repo.WorkspaceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepo workspaceRepo;

    //Create a workspace
    public WorkspaceResponse createWorkspace(String w_name, String w_description, String w_slug){
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
        return  new WorkspaceResponse(
                saved.getW_id(),
                saved.getName(),
                saved.getDescription(),
                saved.getSlug());
    }


    //List all workspaces
    private List<WorkspaceResponse> listWorkspaces(){
        return workspaceRepo.findAll()
                .stream()
                .map(workspace -> new WorkspaceResponse(
                        workspace.getW_id(),
                        workspace.getName(),
                        workspace.getDescription(),
                        workspace.getSlug()
                ))
                .toList();
    }


    //Update a workspace
    private WorkspaceResponse updateWorkspaceName(UUID w_id, String name){
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
        return new WorkspaceResponse(
                saved.getW_id(),
                saved.getName(),
                saved.getDescription(),
                saved.getSlug()
                );
    }


    //List all ACTIVE workspaces



}
