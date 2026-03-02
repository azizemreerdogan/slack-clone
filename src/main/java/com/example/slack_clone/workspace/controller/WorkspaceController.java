package com.example.slack_clone.workspace.controller;

import com.example.slack_clone.workspace.dto.CreateWorkspaceRequestDto;
import com.example.slack_clone.workspace.dto.UpdateWorkspaceRequestDto;
import com.example.slack_clone.workspace.dto.WorkspaceResponseDto;
import com.example.slack_clone.workspace.workspace_service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspace")
public class WorkspaceController {
    final WorkspaceService workspaceService;

    //Fetch all workspaces
    @GetMapping
    public ResponseEntity<List<WorkspaceResponseDto>> listWorkspaces(){
        List<WorkspaceResponseDto> workspaceResponse = workspaceService.listWorkspaces();
        return ResponseEntity.ok(workspaceResponse);
    }

    /**Update a workspace
    *PUT /api/workspace/{w_id}
     * @param w_id Workspace id
     * @param request Workspace Request
     * @return Updated Workspace Response
     */
    @PatchMapping("/{w_id}")
    public ResponseEntity<WorkspaceResponseDto> updateWorkspace(
            @PathVariable("w_id") UUID w_id,
            @Valid @RequestBody UpdateWorkspaceRequestDto request){
        log.info("Update Workspace request received.");
        WorkspaceResponseDto upd_request = workspaceService.updateWorkspace(w_id, request);
        log.info("Workspace updated successfully: {}", upd_request);
        return ResponseEntity.ok(upd_request);
    }

    //Create a Workspace
    @PostMapping()
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(@Valid @RequestBody CreateWorkspaceRequestDto request){
        log.info("Create Workspace request received: {}", request);
        WorkspaceResponseDto response = workspaceService.createWorkspace(
                request.name(),
                request.description(),
                request.slug());
        log.info("Workspace created successfully: {}", response);
        return ResponseEntity.ok(response);
    }


    //List active workspaces

    //Delete a workspace
    //Don't recommend using this endpoint since we are saving audit data.
    @DeleteMapping("/{w_id}")
    public ResponseEntity<WorkspaceResponseDto> deleteWorkspace(@PathVariable("w_id") UUID w_id){
        log.info("Delete Workspace request received for id: {}", w_id);
        WorkspaceResponseDto response = workspaceService.deleteWorkspace(w_id);
        log.info("Workspace deleted successfully: {}", response);
        return ResponseEntity.ok(response);
    }

}
