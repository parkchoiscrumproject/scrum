package com.parkchoi.scrum.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "유저 상태메시지 변경 응답 DTO")
public class UserStatusMessageUpdateResponseDTO {
    private String statusMessage;

    public UserStatusMessageUpdateResponseDTO(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
