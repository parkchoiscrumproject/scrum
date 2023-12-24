package com.parkchoi.scrum.domain.user.entity;
import com.parkchoi.scrum.domain.team.entity.InviteTeamList;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String type;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false, length = 20)
    private String nickname;
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedDate;
    @Column(length = 100, nullable = true)
    private String statusMessage;
    @Column(nullable = false)
    private String profileImage;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<InviteTeamList> inviteTeamList = new ArrayList<>();

    public User() {
    }

    @Builder
    public User(Long id, String type, String email, String nickname, String statusMessage, String profileImage) {
        this.id = id;
        this.type = type;
        this.email = email;
        this.nickname = nickname;
        this.statusMessage = statusMessage;
        this.profileImage = profileImage;
    }
}
