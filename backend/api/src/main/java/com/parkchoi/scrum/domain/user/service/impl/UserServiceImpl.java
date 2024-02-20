package com.parkchoi.scrum.domain.user.service.impl;

import com.parkchoi.scrum.domain.exception.exception.UserException;
import com.parkchoi.scrum.domain.exception.info.UserExceptionInfo;
import com.parkchoi.scrum.domain.log.entity.UserLog;
import com.parkchoi.scrum.domain.log.entity.UserNicknameLog;
import com.parkchoi.scrum.domain.log.entity.UserProfileImageLog;
import com.parkchoi.scrum.domain.log.entity.UserStatusMessageLog;
import com.parkchoi.scrum.domain.log.repository.UserLogRepository;
import com.parkchoi.scrum.domain.log.repository.UserNicknameLogRepository;
import com.parkchoi.scrum.domain.log.repository.UserProfileImageLogRepository;
import com.parkchoi.scrum.domain.log.repository.UserStatusMessageLogRepository;
import com.parkchoi.scrum.domain.user.dto.response.*;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.repository.user.UserRepository;
import com.parkchoi.scrum.domain.user.service.UserService;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import com.parkchoi.scrum.util.s3.S3UploadService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserLogRepository userLogRepository;
    private final UserNicknameLogRepository userNicknameLogRepository;
    private final UserProfileImageLogRepository userProfileImageLogRepository;
    private final UserStatusMessageLogRepository userStatusMessageLogRepository;
    private final S3UploadService s3UploadService;
    private final JwtUtil jwtUtil;


    // 서비스 로그아웃
    @Override
    @Transactional
    public void logout(String accessToken, HttpServletResponse response) {
        Long userId = jwtUtil.getUserId(accessToken);

        User user = findUser(userId);

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
    @Override
    @Transactional
    public UserLoginInfoResponseDTO login(String accessToken, HttpServletRequest request) {
        Long userId = jwtUtil.getUserId(accessToken);

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            UserLog build = UserLog.builder()
                    .user(null)
                    .loginIp(request.getRemoteAddr())
                    .isLoginSuccess(false).build();

            userLogRepository.save(build);

            throw new UserException(UserExceptionInfo.NOT_FOUNT_USER, "DB에서 \" + userId + \"번 유저를 찾지 못했습니다.");
        } else {
            User user = userOptional.get();
            // 로그인 상태 true 변경
            user.isOnlineTrue();

            // 유저 로그인 로그 생성
            UserLog build = UserLog.builder()
                    .user(user)
                    .loginIp(request.getRemoteAddr())
                    .isLoginSuccess(true).build();

            userLogRepository.save(build);

            return UserLoginInfoResponseDTO.fromEntity(user);
        }

    }

    // 닉네임 중복 검사
    @Override
    public boolean checkDuplicationNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // 이메일로 유저 정보 찾기
    @Override
    public UserInviteInfoResponseDTO findUserInfoToEmail(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            return null;
        } else {
            User user = byEmail.get();

            return UserInviteInfoResponseDTO.fromEntity(user);
        }
    }

    // 유저 닉네임 변경
    @Override
    @Transactional
    public UserNicknameUpdateResponseDTO updateUserNickname(String accessToken, String nickname) {
        Long userId = jwtUtil.getUserId(accessToken);

        User user = findUser(userId);

        UserNicknameLog userNicknameLog = UserNicknameLog.fromEntity(user);

        user.updateNickname(nickname);


        userNicknameLogRepository.save(userNicknameLog);

        return new UserNicknameUpdateResponseDTO(nickname);
    }

    // 프로필 이미지 변경
    @Override
    @Transactional
    public UserProfileImageUpdateResponseDTO updateUserProfileImage(String accessToken, MultipartFile file) throws IOException {
        Long userId = jwtUtil.getUserId(accessToken);

        User user = findUser(userId);

        String url = s3UploadService.saveFile(file);

        UserProfileImageLog userProfileImageLog = UserProfileImageLog.fromEntity(user);

        user.updateProfileImage(url);
        userProfileImageLogRepository.save(userProfileImageLog);

        return new UserProfileImageUpdateResponseDTO(url);
    }

    // 상태메시지 변경
    @Override
    @Transactional
    public UserStatusMessageUpdateResponseDTO updateUserStatusMessage(String accessToken, String statusMessage) {
        Long userId = jwtUtil.getUserId(accessToken);

        User user = findUser(userId);

        UserStatusMessageLog userStatusMessageLog = userStatusMessageLogRepository.save(UserStatusMessageLog.fromEntity(user));

        user.updateStatusMessage(statusMessage);

        userStatusMessageLogRepository.save(userStatusMessageLog);

        return new UserStatusMessageUpdateResponseDTO(statusMessage);
    }

    // 유저 Id로 유저 찾기
    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new UserException(UserExceptionInfo.NOT_FOUNT_USER, "DB에서 \" + userId + \"번 유저를 찾지 못했습니다."));
    }

}

