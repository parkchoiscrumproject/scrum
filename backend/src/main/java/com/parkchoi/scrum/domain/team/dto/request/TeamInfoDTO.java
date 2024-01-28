package com.parkchoi.scrum.domain.team.dto.request;

import lombok.Data;

@Data
public class TeamInfoDTO {
    private String name;
    private String description;
    private int maxMember;
}
