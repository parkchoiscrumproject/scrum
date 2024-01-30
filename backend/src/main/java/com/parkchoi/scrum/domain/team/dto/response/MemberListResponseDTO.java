package com.parkchoi.scrum.domain.team.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class MemberListResponseDTO {
    private List<MemberDTO> members;

    public MemberListResponseDTO(List<MemberDTO> members){
        this.members = members;
    }
}
