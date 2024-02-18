package com.parkchoi.scrum.domain.team.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateTeamRequestDTO {
    private TeamInfoDTO teamInfoDTO;
    private List<Long> inviteList;


}


