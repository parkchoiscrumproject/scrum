package com.parkchoi.scrum.domain.team.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class TeamListResponseDTO {
    private List<TeamDTO> teams;

    public TeamListResponseDTO(List<TeamDTO> teams){
        this.teams = teams;
    }
}
