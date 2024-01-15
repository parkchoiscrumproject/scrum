package com.parkchoi.scrum.domain.scrum.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ScrumRoomListResponseDTO {
    private List<ScrumRoomDTO> scrums;

    public ScrumRoomListResponseDTO(List<ScrumRoomDTO> scrums) {
        this.scrums = scrums;
    }
}
