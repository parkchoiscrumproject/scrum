package com.parkchoi.scrum.util;

import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.exception.UserNotFoundException;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetUserId {

    private final UserRepository userRepository;

    public User getUserId(){
        // 시큐리티에서 저장된 정보 꺼냄
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        Long userId = (Long) principal;

        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 정보 없음"));
    }

}
