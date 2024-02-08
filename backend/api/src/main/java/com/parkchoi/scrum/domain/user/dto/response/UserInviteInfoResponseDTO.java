package com.parkchoi.scrum.domain.user.dto.response;

        import io.swagger.v3.oas.annotations.media.Schema;
        import lombok.*;

@Data
@Schema(description = "유저 초대 목록 응답 DTO")
public class UserInviteInfoResponseDTO {

    private Long userId;
    private String profileImage;
    private String nickname;

    @Builder
    public UserInviteInfoResponseDTO(Long userId, String profileImage, String nickname) {
        this.userId = userId;
        this.profileImage = profileImage;
        this.nickname = nickname;
    }
}
