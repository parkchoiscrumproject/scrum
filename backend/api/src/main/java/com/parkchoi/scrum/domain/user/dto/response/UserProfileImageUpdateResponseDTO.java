package com.parkchoi.scrum.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "유저 프로필 사진 변경 응답 DTO")
public class UserProfileImageUpdateResponseDTO {
    private String profileImage;

    public UserProfileImageUpdateResponseDTO(String profileImage) {
        this.profileImage = profileImage;
    }
}
