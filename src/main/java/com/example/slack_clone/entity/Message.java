package com.example.slack_clone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;



@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID msg_id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id",nullable = false)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private WorkspaceMember sender;

    @Column(columnDefinition = "TEXT",updatable = true)
    private String content;

    //Used for Slack message threads, enforcing 1 depth thread should be on SQL or service level
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_msg_id", nullable = true)
    private Message parent_root_id;

    private boolean isEdited= false;
    private boolean isDeleted = false;

    private LocalDateTime createdAt;
    private LocalDateTime editedAt;






}
