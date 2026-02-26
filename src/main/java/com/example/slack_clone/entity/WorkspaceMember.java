package com.example.slack_clone.entity;

import com.example.slack_clone.entity.enums.MemberStatus;
import com.example.slack_clone.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workspace_members")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkspaceMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID mem_id;

    @ManyToOne
    @JoinColumn(name = "user_usr_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_w_id")
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String displayName;

    @Enumerated(EnumType.STRING)
    private MemberStatus member_status;

    private LocalDateTime joined_at;


}
