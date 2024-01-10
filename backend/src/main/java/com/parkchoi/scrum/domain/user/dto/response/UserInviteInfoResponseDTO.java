package com.parkchoi.scrum.domain.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInviteInfoResponseDTO {

    private Long userId;
    private String profileImage;
    private String nickname;

}
