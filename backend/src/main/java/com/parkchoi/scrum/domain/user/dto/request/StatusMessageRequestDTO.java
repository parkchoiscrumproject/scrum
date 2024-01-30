package com.parkchoi.scrum.domain.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StatusMessageRequestDTO {
    @Size(max = 100, message = "상태메시지는 100자를 초과할 수 없습니다.")
    private String message;
}
