package com.parkchoi.scrum.domain.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginInfoResponseDTO {

    private String email;
    private String nickname;
    private String statusMessage;
    private String profileImage;
    private Boolean isOnline;

    public UserLoginInfoResponseDTO() {
    }
}
