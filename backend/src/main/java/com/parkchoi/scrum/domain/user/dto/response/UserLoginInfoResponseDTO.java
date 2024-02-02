package com.parkchoi.scrum.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "유저 로그인 응답 DTO")
public class UserLoginInfoResponseDTO {

    private String email;
    private String nickname;
    private String statusMessage;
    private String profileImage;
    @Schema(description = "접속 여부(true = 접속, false = 미접속)")
    private Boolean isOnline;

}
