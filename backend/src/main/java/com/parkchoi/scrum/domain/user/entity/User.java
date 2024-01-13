package com.parkchoi.scrum.domain.user.entity;

import com.parkchoi.scrum.domain.team.entity.InviteTeamList;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
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
    @Column(nullable = false)
    private Boolean isOnline = false;

    @Builder
    public User(String type, String email, String nickname, String profileImage) {
        this.type = type;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.isOnline = true;
    }

    // 온라인 상태 변경
    public void isOnlineTrue(){
        this.isOnline = true;
    }

    // 오프라인 상태 변경
    public void isOnlineFalse(){
        this.isOnline = false;
    }

    // 닉네임 변경
    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    // 프로필 사진 변경
    public void updateProfileImage(String profileImage){
        this.profileImage = profileImage;
    }

}
