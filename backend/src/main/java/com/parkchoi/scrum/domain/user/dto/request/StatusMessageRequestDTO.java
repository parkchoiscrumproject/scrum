package com.parkchoi.scrum.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "상태 메시지 변경 요청 DTO")
public class StatusMessageRequestDTO {
    @Size(max = 100, message = "상태메시지는 100자를 초과할 수 없습니다.")
    @Schema(description = "상태 메시지")
    private String message;
}
