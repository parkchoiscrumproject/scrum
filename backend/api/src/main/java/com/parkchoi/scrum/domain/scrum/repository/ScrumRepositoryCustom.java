package com.parkchoi.scrum.domain.scrum.repository;

import com.parkchoi.scrum.domain.scrum.dto.request.ScrumSearchCondition;
import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ScrumRepositoryCustom {
    Boolean existsActiveScrumByUser(User user);
    List<Scrum> findActiveScrumsByTeam(Team team);
    Optional<Scrum> findActiveScrumByScrumId(Long scrumId);
    Page<Scrum> searchScrumWithPagination(ScrumSearchCondition condition, Pageable pageable);
}
