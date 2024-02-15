package com.parkchoi.scrum.domain.scrum.repository;

import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.scrum.entity.ScrumParticipant;
import com.parkchoi.scrum.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScrumParticipantRepository extends JpaRepository<ScrumParticipant, Long> {
    // 스크럼과 유저를 포함하는 참여리스트 찾기
    boolean existsScrumParticipantByUserAndScrum(User user, Scrum scrum);
}
