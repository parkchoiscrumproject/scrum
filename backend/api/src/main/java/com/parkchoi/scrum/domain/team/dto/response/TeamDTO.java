package com.parkchoi.scrum.domain.team.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamDTO {
    private Long teamId;
    private String name;
    private int currentMember;
    private String teamProfileImage;
}
