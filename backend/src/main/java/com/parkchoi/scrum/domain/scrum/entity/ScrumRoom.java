package com.parkchoi.scrum.domain.scrum.entity;

import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ScrumRoom {

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

    @Builder
    public ScrumRoom(Team team, User user, String name, int currentMember, int maxMember) {
        this.team = team;
        this.user = user;
        this.name = name;
        this.currentMember = currentMember;
        this.maxMember = maxMember;
    }

    public ScrumRoom() {

    }
}

