package com.parkchoi.scrum.domain.log.repository;

import com.parkchoi.scrum.domain.log.entity.UserNicknameLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNicknameLogRepository extends JpaRepository<UserNicknameLog, Long> {
}
