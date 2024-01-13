package com.parkchoi.scrum.domain.team.dto.request;

import lombok.Data;

@Data
public class CreateTeamRequestDTO {
    private Long userId;
    private String name;
    private String description;
    private int maxMember;
    private String teamProfileImage;
}
