package com.parkchoi.scrum.domain.scrum.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ScrumInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrum_room_id")
    private ScrumRoom scrumRoom;
    @Column(nullable = false)
    private String subject;
    @CreatedDate
    private LocalDateTime startTime;
    @Column(nullable = true)
    private LocalDateTime endTime;

    @Builder
    public ScrumInfo(ScrumRoom scrumRoom, String subject) {
        this.scrumRoom = scrumRoom;
        this.subject = subject;
    }

    public ScrumInfo() {

    }
}
