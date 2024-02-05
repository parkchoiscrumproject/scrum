package com.parkchoi.scrum.domain.team.entity;

import com.parkchoi.scrum.domain.user.entity.User;
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
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 100, nullable = false)
    private String description;
    @Column(nullable = false)
    private int currentMember;
    @Column(nullable = false)
    private int maxMember;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedDate;
    @Column(nullable = false)
    private String teamProfileImage;
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<InviteTeamList> inviteTeamList = new ArrayList<>();


    @Builder
    public Team(String name, String description, int currentMember, int maxMember, User user, String teamProfileImage) {
        this.name = name;
        this.description = description;
        this.currentMember = currentMember;
        this.maxMember = maxMember;
        this.user = user;
        this.teamProfileImage = teamProfileImage;
    }

    public void decreaseCurrentMember(){
        if(this.currentMember > 0){
            this.currentMember -= 1;
        }else{
            throw new IllegalStateException("팀 멤버 수가 0보다 작을 수 없습니다.");
        }
    }

}
