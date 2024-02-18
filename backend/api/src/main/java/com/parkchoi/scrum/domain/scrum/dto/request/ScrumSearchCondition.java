package com.parkchoi.scrum.domain.scrum.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScrumSearchCondition {
    private String name;
    private String leaderName;
}
