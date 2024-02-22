package com.parkchoi.scrum.util;

import com.parkchoi.scrum.domain.exception.exception.UserException;
import com.parkchoi.scrum.domain.exception.info.UserExceptionInfo;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SecurityContext {
    private final UserRepository userRepository;

    // 유저 꺼내기
    public User getUser(){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserExceptionInfo.NOT_FOUNT_USER, "DB에서 \" + userId + \"번 유저를 찾지 못했습니다."));

    }

    // 유저아이디 꺼내기
    public Long getUserId(String accessToken){
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
