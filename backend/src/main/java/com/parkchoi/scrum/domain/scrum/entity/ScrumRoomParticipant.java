package com.parkchoi.scrum.domain.scrum.entity;

import com.parkchoi.scrum.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ScrumRoomParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrum_room_id")
    private ScrumRoom scrumRoom;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public ScrumRoomParticipant(ScrumRoom scrumRoom, User user) {
        this.scrumRoom = scrumRoom;
        this.user = user;
    }

    public ScrumRoomParticipant() {

    }
}
