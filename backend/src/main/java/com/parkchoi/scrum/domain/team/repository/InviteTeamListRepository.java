package com.parkchoi.scrum.domain.team.repository;

import com.parkchoi.scrum.domain.team.entity.InviteTeamList;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InviteTeamListRepository extends JpaRepository<InviteTeamList, Long> {
    Optional<InviteTeamList> findByUserAndTeamAndParticipantIsTrue(User user, Team team);

    Optional<List<InviteTeamList>> findByUserAndParticipantIsTrue(User user);

    Optional<List<InviteTeamList>> findByTeamAndParticipantIsTrue(Team team);

}
