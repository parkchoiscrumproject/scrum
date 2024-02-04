package com.parkchoi.scrum.domain.scrum.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScrumRoomDTO {
    private Long scrumId;
    @Schema(description = "스크럼 제목")
    private String name;
    @Schema(description = "리더 프로필 사진")
    private String profileImage;
    @Schema(description = "리더 닉네임")
    private String nickname;
    @Schema(description = "스크럼 현재 참여 인원")
    private int currentMember;
    @Schema(description = "스크럼 최대 참여 인원")
    private int maxMember;
    @Schema(description = "스크럼 진행 상태, true = 진행중 or 진행완료, flase = 진행전")
    private Boolean isRunning;
}
