package com.parkchoi.scrum.domain.scrum.entity;

import com.parkchoi.scrum.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class ScrumParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrum_id")
    @NotNull(message = "스크럼은 필수입니다.")
    private Scrum scrum;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull(message = "유저는 필수입니다.")
    private User user;

    @Builder
    public ScrumParticipant(Scrum scrum, User user) {
        this.scrum = scrum;
        this.user = user;
    }

}
