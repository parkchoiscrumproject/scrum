package com.parkchoi.scrum.domain.user.entity;

import com.parkchoi.scrum.domain.team.entity.InviteTeamList;
import com.parkchoi.scrum.util.api.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String providerId;
    @Column(unique = true, nullable = false)
    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 항상 입력돼야 합니다.")
    private String email;
    @Column(unique = true, nullable = false, length = 20)
    @NotBlank(message = "닉네임은 항상 입력돼야 합니다.")
    private String nickname;
    @Column(length = 100, nullable = true)
    @Size(max = 100, message = "상태메시지는 100자를 초과할 수 없습니다.")
    private String statusMessage;
    @Column(nullable = false)
    private String profileImage;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<InviteTeamList>  inviteTeamList = new ArrayList<>();
    @Column(nullable = false)
    private Boolean isOnline = false;

    @Builder
    public User(String type, String email, String nickname, String profileImage, Boolean isOnline, String providerId) {
        this.type = type;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.isOnline = isOnline;
        this.providerId = providerId;
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

    // 상태메시지 변경
    public void updateStatusMessage(String message){
        this.statusMessage = message;
    }

}
