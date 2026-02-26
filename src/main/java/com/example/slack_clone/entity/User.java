package com.example.slack_clone.entity;

import com.example.slack_clone.entity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID usr_id;
    private String name;

    @Column(unique = true)
    private String email;
    private String password;
    private boolean isEmailVerified;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    @Enumerated(EnumType.STRING)
    private UserStatus user_status;
}
