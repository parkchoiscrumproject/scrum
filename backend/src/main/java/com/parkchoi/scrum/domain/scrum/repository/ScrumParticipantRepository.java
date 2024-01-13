package com.parkchoi.scrum.domain.scrum.repository;

import com.parkchoi.scrum.domain.scrum.entity.ScrumParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrumParticipantRepository extends JpaRepository<ScrumParticipant, Long> {
}
