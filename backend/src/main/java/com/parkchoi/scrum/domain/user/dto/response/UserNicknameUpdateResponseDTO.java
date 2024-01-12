package com.parkchoi.scrum.domain.user.dto.response;

import lombok.Data;

@Data
public class UserNicknameUpdateResponseDTO {
    private String nickname;

    public UserNicknameUpdateResponseDTO(String nickname) {
        this.nickname = nickname;
    }
}
