package com.parkchoi.scrum.domain.log.repository;

import com.parkchoi.scrum.domain.log.entity.UserStatusMessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusMessageLogRepository extends JpaRepository<UserStatusMessageLog, Long> {
}
