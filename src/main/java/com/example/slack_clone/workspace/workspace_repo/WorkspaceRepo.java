package com.example.slack_clone.workspace.workspace_repo;


import com.example.slack_clone.workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


//Repo for Workspace-related operations

@Repository
public interface WorkspaceRepo extends JpaRepository<Workspace, UUID> {


    Optional<Workspace> findByName(String name);
}
