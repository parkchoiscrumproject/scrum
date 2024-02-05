package com.parkchoi.scrum.domain.log.repository;

import com.parkchoi.scrum.domain.log.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Long> {
}
