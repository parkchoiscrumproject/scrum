package com.parkchoi.scrum.domain.team.repository;

import com.parkchoi.scrum.domain.team.entity.InviteTeamList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteTeamListRepository extends JpaRepository<InviteTeamList, Long> {
}
