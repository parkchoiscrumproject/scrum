package com.parkchoi.scrum.domain.user.service;

import com.parkchoi.scrum.domain.log.entity.UserLog;
import com.parkchoi.scrum.domain.log.repository.UserLogRepository;
import com.parkchoi.scrum.domain.user.dto.response.UserInfoResponseDTO;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.exception.AuthFailException;
import com.parkchoi.scrum.domain.user.exception.UserNotFoundException;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserLogRepository userLogRepository;
    private final JwtUtil jwtUtil;


    // 서비스 로그인
    @Transactional
    public UserInfoResponseDTO getUserInfo(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            throw new AuthFailException("쿠키 존재하지 않음");
        }
        String accessToken = null;

        for (Cookie c : cookies) {
            if (c.getName().equals("accessToken")) {
                accessToken = c.getValue();
                break;
            }
        }

        // 쿠키에 토큰 존재여부 파악
        if (accessToken == null) {
            throw new AuthFailException("쿠키에 토큰 존재하지 않음");
        } else {
            Long userId = jwtUtil.getUserId(accessToken);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("유저 없음"));

            // 유저 로그인 로그 생성
            UserLog build = UserLog.builder()
                    .user(user).build();
            userLogRepository.save(build);

            UserInfoResponseDTO userInfoDTO = UserInfoResponseDTO.builder()
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .profileImage(user.getProfileImage())
                    .statusMessage(user.getStatusMessage())
                    .isOnline(user.getIsOnline()).build();

            return userInfoDTO;
        }
    }
}

