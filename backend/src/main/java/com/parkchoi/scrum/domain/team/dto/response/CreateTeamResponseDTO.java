package com.parkchoi.scrum.domain.team.dto.response;

import lombok.Data;

@Data
public class CreateTeamResponseDTO {
    private String name;
    private String imageUrl;

    public CreateTeamResponseDTO(String name, String imagegUrl){
        this.name = name;
        this.imageUrl = imagegUrl;
    }
}
