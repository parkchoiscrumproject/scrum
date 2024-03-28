package com.parkchoi.scrum.domain.log.repository;

import com.parkchoi.scrum.domain.log.entity.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Long> {
}
