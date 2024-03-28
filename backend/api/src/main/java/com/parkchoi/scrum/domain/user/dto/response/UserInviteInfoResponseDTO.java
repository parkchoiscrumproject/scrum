package com.parkchoi.scrum.domain.user.dto.response;

        import com.parkchoi.scrum.domain.user.entity.User;
        import io.swagger.v3.oas.annotations.media.Schema;
        import lombok.*;

@Getter
@NoArgsConstructor
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

    public static UserInviteInfoResponseDTO fromEntity(User user){
        return UserInviteInfoResponseDTO.builder()
                .nickname(user.getNickname())
                .userId(user.getId())
                .profileImage(user.getProfileImage()).build();
    }
}
