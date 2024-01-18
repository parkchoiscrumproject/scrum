package com.parkchoi.scrum.domain.user.service;

import com.parkchoi.scrum.domain.log.entity.UserLog;
import com.parkchoi.scrum.domain.log.repository.UserLogRepository;
import com.parkchoi.scrum.domain.user.dto.request.UserInfoUpdateRequestDTO;
import com.parkchoi.scrum.domain.user.dto.response.UserNicknameUpdateResponseDTO;
import com.parkchoi.scrum.domain.user.dto.response.UserInviteInfoResponseDTO;
import com.parkchoi.scrum.domain.user.dto.response.UserLoginInfoResponseDTO;
import com.parkchoi.scrum.domain.user.dto.response.UserProfileImageUpdateResponseDTO;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.exception.UserNotFoundException;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import com.parkchoi.scrum.util.s3.S3UploadService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserLogRepository userLogRepository;
    private final S3UploadService s3UploadService;
    private final JwtUtil jwtUtil;


    // 서비스 로그아웃
    @Transactional
    public void logout(String accessToken, HttpServletResponse response){
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        user.isOnlineFalse();

        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        Cookie jsessionidCookie = new Cookie("JSESSIONID", null);
        jsessionidCookie.setMaxAge(0);
        jsessionidCookie.setPath("/");
        response.addCookie(jsessionidCookie);
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

    @Transactional
    // 유저 닉네임 변경
    public UserNicknameUpdateResponseDTO updateUserNickname(String accessToken, String nickname){
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        user.updateNickname(nickname);

        return new UserNicknameUpdateResponseDTO(nickname);
    }

    @Transactional
    public UserProfileImageUpdateResponseDTO updateUserProfileImage(String accessToken, MultipartFile file) throws IOException {
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        String url = s3UploadService.saveFile(file);

        user.updateProfileImage(url);

        return new UserProfileImageUpdateResponseDTO(url);
    }


}

