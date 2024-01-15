package com.parkchoi.scrum.domain.team.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreateTeamRequestDTO {
    private Long userId;
    private TeamInfoDTO teamInfoDTO;
    private List<Long> inviteList;

}


