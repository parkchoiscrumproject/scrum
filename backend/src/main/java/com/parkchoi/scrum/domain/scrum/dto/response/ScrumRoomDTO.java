package com.parkchoi.scrum.domain.scrum.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScrumRoomDTO {
    private Long scrumId;
    private String name;
    private String profileImage;
    private String nickname;
    private int currentMember;
    private int maxMember;
}
