package com.parkchoi.scrum.domain.scrum.dto.request;

import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.scrum.entity.ScrumParticipant;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "스크럼 생성 요청 DTO")
public class CreateScrumRequestDTO {
    @NotBlank(message = "스크럼의 제목은 필수입니다.")
    @Size(max = 20, message = "스크럼 제목은 20자까지 가능합니다.")
    @Pattern(regexp = "^[가-힣A-Za-z0-9]+$", message = "스크럼 제목은 한글, 영어, 숫자만 가능합니다.")
    @Schema(description = "스크럼 제목")
    private String name;
    @Max(value = 15, message = "스크럼 최대 인원은 15명까지 가능합니다.")
    @Schema(description = "스크럼 최대 인원")
    @NotNull(message = "최대 인원은 필수입니다.")
    private int maxMember;
    @Pattern(regexp = "^[가-힣A-Za-z0-9]+$", message = "주제는 한글, 영어, 숫자만 가능합니다.")
    @Size(max = 20, message = "주제는 최대 20자까지 가능합니다.")
    @NotBlank(message = "주제는 필수입니다.")
    @Schema(description = "스크럼 주제")
    private String subject;

    @Builder
    public CreateScrumRequestDTO(String name, int maxMember, String subject) {
        this.name = name;
        this.maxMember = maxMember;
        this.subject = subject;
    }

    // 스크럼 생성
    public Scrum toEntity(CreateScrumRequestDTO dto, User user, Team team){
        return Scrum.builder()
                .user(user)
                .team(team)
                .currentMember(1)
                .subject(dto.getSubject())
                .name(dto.getName())
                .maxMember(dto.getMaxMember())
                .build();
    }
}
