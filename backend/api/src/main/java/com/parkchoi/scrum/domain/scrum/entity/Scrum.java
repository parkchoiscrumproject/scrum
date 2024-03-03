package com.parkchoi.scrum.domain.scrum.entity;

import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @Column(nullable = false)
    @Pattern(regexp = "^[가-힣A-Za-z0-9]+$", message = "주제는 한글, 영어, 숫자만 가능합니다.")
    @Size(max = 20, message = "주제는 최대 20자까지 가능합니다.")
    @NotBlank(message = "주제는 필수입니다.")
    private String subject;
    @Column(nullable = true)
    private LocalDateTime startTime;
    @Column(nullable = true)
    private LocalDateTime endTime;
    @Column(nullable = false)
    private Boolean isStart = false;

    @Builder
    public Scrum(Team team, User user, String name, int currentMember, int maxMember, String subject) {
        this.team = team;
        this.user = user;
        this.name = name;
        this.currentMember = currentMember;
        this.maxMember = maxMember;
        this.subject = subject;
    }


    public void plusCurrentMember(){
        this.currentMember = currentMember + 1;
    }

    public void deleteScrum(){
        this.deleteDate = LocalDateTime.now();
        this.isStart = true;
    }

    public void startScrum(){
        this.isStart = true;
        this.startTime = LocalDateTime.now();
    }

    public void endScrum(){
        this.endTime = LocalDateTime.now();
        this.isStart = true;
    }

}

