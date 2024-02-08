package com.parkchoi.scrum.domain.scrum.repository;

import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ScrumRepository extends JpaRepository<Scrum, Long> {
    // 삭제되지 않고 현재 팀에 속한 스크럼 모두 조회
    @Query("SELECT s FROM Scrum s JOIN FETCH s.user WHERE s.team = :team AND s.deleteDate IS NULL")
    List<Scrum> findByTeamWithFetchJoinUserAndDeleteDateIsNull(@Param("team") Team team);

    // 삭제되지 않고 현재 유저가 속한 스크럼 모두 조회
    @Query("SELECT s FROM Scrum s WHERE s.user = :user AND s.deleteDate IS NULL AND s.endTime IS NULL")
    List<Scrum> findByUserWithAndDeleteDateIsNullAndEndTimeIsNull(@Param("user") User user);
}