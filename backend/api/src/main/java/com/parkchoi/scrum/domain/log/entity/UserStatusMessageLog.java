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
public class UserStatusMessageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @CreatedDate
    private LocalDateTime changeDate;
    @Column(nullable = true)
    private String previousStatusMessage;

    @Builder
    public UserStatusMessageLog(User user, String previousStatusMessage) {
        this.user = user;
        this.previousStatusMessage = previousStatusMessage;
    }

}


