package com.parkchoi.scrum.domain.log.repository;

import com.parkchoi.scrum.domain.log.entity.UserProfileImageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileImageLogRepository extends JpaRepository<UserProfileImageLog, Long> {
}
