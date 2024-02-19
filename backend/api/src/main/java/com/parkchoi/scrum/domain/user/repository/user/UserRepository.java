package com.parkchoi.scrum.domain.user.repository.user;

import com.parkchoi.scrum.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일 찾기
    Optional<User> findByEmail(String email);

    // 닉네임 존재 여부
    Boolean existsByNickname(String nickname);

    // 닉네임으로 찾기
    Optional<User> findByNickname(String nickname);

}
