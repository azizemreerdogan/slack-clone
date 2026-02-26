package com.example.slack_clone.entity;


import com.example.slack_clone.entity.enums.EntityType;
import com.example.slack_clone.entity.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID notif_id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_w_id")
    private WorkspaceMember workspaceMember;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type",nullable = false)
    private EntityType entityType;

    private UUID entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type",nullable = false)
    private NotificationType notificationType;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }

    private boolean isRead;


}
