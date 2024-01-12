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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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


    // 서비스 로그아웃
    @Transactional
    public void logout(String accessToken, HttpServletResponse response){
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        user.isOnlineFalse();

        Cookie accessTokenCookie = new Cookie("accessToken", null); // "accessToken"은 삭제하려는 쿠키의 이름
        accessTokenCookie.setMaxAge(0); // 쿠키의 유효기간을 0으로 설정
        accessTokenCookie.setPath("/"); // 쿠키의 경로 설정, 이는 쿠키를 설정한 동일한 경로여야 합니다
        response.addCookie(accessTokenCookie); // 응답에 쿠키 추가

        Cookie refreshTokenCookie = new Cookie("refreshToken", null); // "accessToken"은 삭제하려는 쿠키의 이름
        refreshTokenCookie.setMaxAge(0); // 쿠키의 유효기간을 0으로 설정
        refreshTokenCookie.setPath("/"); // 쿠키의 경로 설정, 이는 쿠키를 설정한 동일한 경로여야 합니다
        response.addCookie(refreshTokenCookie); // 응답에 쿠키 추가
    }

    // 서비스 로그인
    @Transactional
    public UserLoginInfoResponseDTO getUserInfo(String accessToken) {
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
        return userRepository.existsByNickname(nickname);
    }

    // 이메일로 유저 정보 찾기
    public UserInviteInfoResponseDTO findUserInfoToEmail(String accessToken, String email){
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

