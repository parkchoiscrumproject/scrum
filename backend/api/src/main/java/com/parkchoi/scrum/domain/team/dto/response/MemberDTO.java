package com.parkchoi.scrum.domain.team.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDTO {
    private Long userId;
    private String nickname;
    private String statusMessage;
    private String profileImage;
    private Boolean isOnline;
}
