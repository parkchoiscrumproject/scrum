package com.parkchoi.scrum.domain.scrum.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScrumSearchCondition {
    private String name;
    private String leaderName;
}
