package com.parkchoi.scrum.domain.team.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamInfoDTO {
    private String name;
    private String description;
    private int maxMember;
}
