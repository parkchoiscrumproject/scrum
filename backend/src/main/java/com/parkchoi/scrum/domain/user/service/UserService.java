package com.parkchoi.scrum.domain.user.service;

import com.parkchoi.scrum.domain.log.entity.UserLog;
import com.parkchoi.scrum.domain.log.repository.UserLogRepository;
import com.parkchoi.scrum.domain.user.dto.response.UserInfoResponseDTO;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.exception.UserNotFoundException;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
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
        String accessToken = jwtUtil.getAccessToken(request);

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

    // 닉네임 중복 검사
    public boolean checkDuplicationNickname(HttpServletRequest request, String nickname) {
        String accessToken = jwtUtil.getAccessToken(request);

        return userRepository.existsByNickname(nickname);
    }
}

