package com.example.slack_clone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "channel_member")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//Entity to handle N-N relationship between Channel and Workspace Member
public class ChannelMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID chmem_id;

    @ManyToOne
    @JoinColumn(name = "channel_channel_id", nullable = false)
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "workspace_members_mem_id",nullable = false)
    private WorkspaceMember workspaceMember;
}
