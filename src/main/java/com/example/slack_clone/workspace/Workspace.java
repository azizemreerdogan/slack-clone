package com.example.slack_clone.workspace;


import com.example.slack_clone.entity.enums.WorkspaceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workspace")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID w_id;

    @Column(name = "name",nullable= false)
    private String name;
    private String description;
    private String slug;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private WorkspaceStatus status = WorkspaceStatus.ACTIVE;

}



