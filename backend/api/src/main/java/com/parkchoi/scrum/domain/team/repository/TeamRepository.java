package com.parkchoi.scrum.domain.team.repository;

import com.parkchoi.scrum.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    //팀 삭제
    void deleteByIdAndUserId(Long id, Long userId);


}
