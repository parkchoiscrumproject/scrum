package com.parkchoi.scrum.domain.scrum.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@ToString
public class ScrumInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrum_id")
    @NotNull(message = "스크럼은 필수입니다.")
    private Scrum scrum;
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
    private Boolean isStart;

    @Builder
    public ScrumInfo(Scrum scrum, String subject, Boolean isStart) {
        this.scrum = scrum;
        this.subject = subject;
        this.isStart = isStart;
    }

    public void startScrum(){
        this.isStart = true;
        this.startTime = LocalDateTime.now();
    }

    public void endScrum(){
        this.endTime = LocalDateTime.now();
    }

}