package com.parkchoi.scrum.domain.team.repository;

import com.parkchoi.scrum.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    //팀 삭제
    @Modifying
    @Query("delete from Team t where t.id = :teamId and t.user.id = :userId")
    void deleteByIdAndUserId(Long teamId, Long userId);


}
