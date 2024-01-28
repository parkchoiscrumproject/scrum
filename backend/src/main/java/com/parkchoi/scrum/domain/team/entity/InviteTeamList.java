package com.parkchoi.scrum.domain.team.entity;

import com.parkchoi.scrum.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class InviteTeamList {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
    @Column(nullable = false)
    private boolean participant;

    @Builder
    public InviteTeamList(User user, Team team, boolean participant) {
        this.user = user;
        this.team = team;
        this.participant = participant;
    }

    public void acceptInvitation(){
        this.participant = true;
    }


}
