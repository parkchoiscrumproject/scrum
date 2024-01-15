package com.parkchoi.scrum.domain.scrum.repository;

import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrumRepository extends JpaRepository<Scrum, Long> {
    @Query("SELECT s FROM Scrum s JOIN FETCH s.user WHERE s.team = :team")
    Optional<List<Scrum>> findByTeamWithUserFetchJoin(Team team);
}
