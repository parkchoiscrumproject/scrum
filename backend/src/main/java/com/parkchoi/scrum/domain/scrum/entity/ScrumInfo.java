package com.parkchoi.scrum.domain.scrum.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class ScrumInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrum_id")
    private Scrum scrum;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = true)
    private LocalDateTime startTime;
    @Column(nullable = true)
    private LocalDateTime endTime;
    @Column(nullable = false)
    private Boolean isStart;

    @Builder
    public ScrumInfo(Scrum scrum, String subject, Boolean isStart) {
        this.scrum = scrum;
        this.subject = subject;
        this.isStart = isStart;
    }

}
