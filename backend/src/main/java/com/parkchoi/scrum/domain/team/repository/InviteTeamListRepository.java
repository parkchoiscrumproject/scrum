package com.parkchoi.scrum.domain.team.repository;

import com.parkchoi.scrum.domain.team.entity.InviteTeamList;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteTeamListRepository extends JpaRepository<InviteTeamList, Long> {
    Optional<InviteTeamList> findByUserAndTeamAndParticipantIsTrue(User user, Team team);

    void deleteByTeam(Team team);
}
