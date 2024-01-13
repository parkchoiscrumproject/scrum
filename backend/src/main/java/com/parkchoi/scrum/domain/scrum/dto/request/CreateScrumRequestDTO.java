package com.parkchoi.scrum.domain.scrum.dto.request;

import lombok.Data;

@Data
public class CreateScrumRequestDTO {
    private Long userId;
    private String name;
    private int maxMember;
    private String subject;
}
