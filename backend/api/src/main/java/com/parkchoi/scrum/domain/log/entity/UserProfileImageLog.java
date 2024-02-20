package com.parkchoi.scrum.domain.log.entity;

import com.parkchoi.scrum.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class UserProfileImageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @CreatedDate
    private LocalDateTime changeDate;
    private String previousProfileImage;

    @Builder
    public UserProfileImageLog(User user, String previousProfileImage) {
        this.user = user;
        this.previousProfileImage = previousProfileImage;
    }

    public static UserProfileImageLog fromEntity(User user) {
        return UserProfileImageLog.builder()
                .user(user)
                .previousProfileImage(user.getProfileImage()).build();
    }
}
