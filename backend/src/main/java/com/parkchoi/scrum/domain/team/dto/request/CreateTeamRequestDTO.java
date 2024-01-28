package com.parkchoi.scrum.domain.team.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateTeamRequestDTO {
    private TeamInfoDTO teamInfoDTO;
    private List<Long> inviteList;

}


