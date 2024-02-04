package com.parkchoi.scrum.domain.scrum.entity;

import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class Scrum {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    @NotNull(message = "팀은 필수입니다.")
    private Team team;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull(message = "유저는 필수입니다.")
    private User user;
    @Column(nullable = false)
    @NotBlank(message = "스크럼의 제목은 필수입니다.")
    @Size(max = 20, message = "스크럼 제목은 20자까지 가능합니다.")
    private String name;
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @Column(nullable = true)
    private LocalDateTime deleteDate;
    @Column(nullable = false)
    private int currentMember;
    @Column(nullable = false)
    @Max(value = 15, message = "스크럼 최대 인원은 15명까지 가능합니다.")
    private int maxMember;
    @OneToOne(mappedBy = "scrum", fetch = FetchType.LAZY)
    @NotNull(message = "스크럼 정보는 필수입니다.")
    private ScrumInfo scrumInfo;

    @Builder
    public Scrum(Team team, User user, String name, int currentMember, int maxMember) {
        this.team = team;
        this.user = user;
        this.name = name;
        this.currentMember = currentMember;
        this.maxMember = maxMember;
    }


    public void plusCurrentMember(){
        this.currentMember = currentMember + 1;
    }

    public void addDeleteDate(){
        this.deleteDate = LocalDateTime.now();
    }

    public void addScrumInfo(ScrumInfo scrumInfo){
        this.scrumInfo = scrumInfo;
    }
}

