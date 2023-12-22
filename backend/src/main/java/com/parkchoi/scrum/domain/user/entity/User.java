package com.parkchoi.scrum.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    private String id;
    private String type;
    private String email;
    private String nickname;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;
    private String statusMessage;
    private String profileImage;

    @Builder
    public User(String id, String type, String email, String nickname, String statusMessage, String profileImage) {
        this.id = id;
        this.type = type;
        this.email = email;
        this.nickname = nickname;
        this.statusMessage = statusMessage;
        this.profileImage = profileImage;
    }

    public User() {

    }
}
