package com.parkchoi.scrum.domain.user.service;

import com.parkchoi.scrum.domain.log.entity.UserLog;
import com.parkchoi.scrum.domain.log.repository.UserLogRepository;
import com.parkchoi.scrum.domain.user.dto.response.UserInviteInfoResponseDTO;
import com.parkchoi.scrum.domain.user.dto.response.UserLoginInfoResponseDTO;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.exception.AuthFailException;
import com.parkchoi.scrum.domain.user.exception.UserNotFoundException;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserLogRepository userLogRepository;
    private final JwtUtil jwtUtil;


    // 서비스 로그인
    @Transactional
    public UserLoginInfoResponseDTO getUserInfo(String accessToken) {
        if(accessToken == null){
            throw new AuthFailException("액세스 토큰 존재하지 않음");
        }

        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        // 로그인 상태 true 변경
        user.isOnlineTrue();

        // 유저 로그인 로그 생성
        UserLog build = UserLog.builder()
                .user(user).build();
        userLogRepository.save(build);

        UserLoginInfoResponseDTO userInfoDTO = UserLoginInfoResponseDTO.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .statusMessage(user.getStatusMessage())
                .isOnline(user.getIsOnline()).build();

        return userInfoDTO;
    }

    // 닉네임 중복 검사
    public boolean checkDuplicationNickname(String accessToken, String nickname) {
        if(accessToken == null){
            throw new AuthFailException("액세스 토큰 존재하지 않음");
        }

        return userRepository.existsByNickname(nickname);
    }

    // 이메일로 유저 정보 찾기
    public UserInviteInfoResponseDTO findUserInfoToEmail(String accessToken, String email){
        if(accessToken == null){
            throw new AuthFailException("액세스 토큰 존재하지 않음");
        }

        Optional<User> byEmail = userRepository.findByEmail(email);
        if(byEmail.isEmpty()){
            return null;
        }else{
            User user = byEmail.get();

            UserInviteInfoResponseDTO build = UserInviteInfoResponseDTO.builder()
                    .userId(user.getId())
                    .profileImage(user.getProfileImage())
                    .nickname(user.getNickname())
                    .build();

            return build;
        }

    }
}

