package com.parkchoi.scrum.domain.scrum.entity;

import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Scrum {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false)
    private String name;
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @Column(nullable = true)
    private LocalDateTime deleteDate;
    @Column(nullable = false)
    private int currentMember;
    @Column(nullable = false)
    private int maxMember;
    @OneToMany(mappedBy = "scrum", fetch = FetchType.LAZY)
    private List<ScrumInfo> scrumInfos = new ArrayList<>();
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
}

