package com.parkchoi.scrum.domain.user.service;

import com.parkchoi.scrum.domain.user.dto.response.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    // 로그아웃
    void logout(String accessToken, HttpServletResponse response);
    // 로그인
    UserLoginInfoResponseDTO login(String accessToken, HttpServletRequest request);
    // 닉네임 중복 검사
    boolean checkDuplicationNickname(String nickname);
    // 이메일로 유저 검색
    UserInviteInfoResponseDTO findUserInfoToEmail(String email);
    // 유저 닉네임 변경
    UserNicknameUpdateResponseDTO updateUserNickname(String accessToken, String nickname);
    // 프로필 이미지 변경
    UserProfileImageUpdateResponseDTO updateUserProfileImage(String accessToken, MultipartFile file) throws IOException;
    // 상태메시지 변경
    UserStatusMessageUpdateResponseDTO updateUserStatusMessage(String accessToken, String statusMessage);

}
