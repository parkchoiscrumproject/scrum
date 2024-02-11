package com.parkchoi.scrum.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "유저 로그인 응답 DTO")
public class UserLoginInfoResponseDTO {

    private String email;
    private String nickname;
    private String statusMessage;
    private String profileImage;
    @Schema(description = "접속 여부(true = 접속, false = 미접속)")
    private Boolean isOnline;

    @Builder
    public UserLoginInfoResponseDTO(String email, String nickname, String statusMessage, String profileImage, Boolean isOnline) {
        this.email = email;
        this.nickname = nickname;
        this.statusMessage = statusMessage;
        this.profileImage = profileImage;
        this.isOnline = isOnline;
    }
}
