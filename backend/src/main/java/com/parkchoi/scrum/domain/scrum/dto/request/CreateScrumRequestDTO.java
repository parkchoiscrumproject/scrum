package com.parkchoi.scrum.domain.scrum.dto.request;

import lombok.Data;

@Data
public class CreateScrumRequestDTO {
    private String name;
    private int maxMember;
    private String subject;

    public CreateScrumRequestDTO(String name, int maxMember, String subject) {
        this.name = name;
        this.maxMember = maxMember;
        this.subject = subject;
    }
}
