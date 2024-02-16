package com.parkchoi.scrum.domain.scrum.repository;

import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // 삭제 되지 않고, 진행이 종료되지 않은 유저의 스크럼 개수 판단(0보다 크면 true)
    boolean existsByUserAndDeleteDateIsNullAndEndTimeIsNull(User user);

    // 종료가 되지 않고, 삭제되지 않은 스크럼 검색
    @Query("SELECT s FROM Scrum s WHERE s.id = :scrumId AND s.deleteDate IS NULL AND s.endTime IS NULL")
    Optional<Scrum> findNotEndAndNotDeleteScrum(@Param("scrumId")Long scrumId);

    // 제목으로 검색
    @Query("SELECT s FROM Scrum s WHERE s.name = :name AND s.deleteDate IS NULL AND s.endTime IS NULL")
    Page<Scrum> findByName(@Param("name") String name, Pageable pageable);

    // 리더로 검색
    @Query("SELECT s FROM Scrum s WHERE s.user = :user AND s.deleteDate IS NULL AND s.endTime IS NULL")
    Page<Scrum> findByUser(@Param("user") User user, Pageable pageable);
}
