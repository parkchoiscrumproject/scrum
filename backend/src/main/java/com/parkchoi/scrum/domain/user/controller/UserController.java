package com.parkchoi.scrum.domain.user.controller;

import com.parkchoi.scrum.domain.user.dto.request.StatusMessageRequestDTO;
import com.parkchoi.scrum.domain.user.dto.response.*;
import com.parkchoi.scrum.domain.user.service.UserService;
import com.parkchoi.scrum.util.api.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "01.User", description = "유저 API")
@Validated
public class UserController implements UserApi{

    private final UserService userService;

    // 유저 로그아웃
    public ResponseEntity<ApiResponse<?>> logout(String accessToken, HttpServletResponse response){
        userService.logout(accessToken, response);

        return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("로그아웃 성공"));
    }

    // 유저 로그인 실행
    public ResponseEntity<ApiResponse<?>> login(String accessToken) {
        UserLoginInfoResponseDTO userInfo = userService.getUserInfo(accessToken);

        return ResponseEntity.status(201).body(ApiResponse.createSuccess(userInfo, "로그인 성공"));
    }

    // 닉네임 중복 검사
    public ResponseEntity<ApiResponse<?>> checkDuplicationNickname(String nickname) {
        boolean result = userService.checkDuplicationNickname(nickname);

        if (result) {
            return ResponseEntity.status(200).body(ApiResponse.createSuccess(result, "닉네임 중복 검사 성공(사용 불가)"));
        } else {
            return ResponseEntity.status(200).body(ApiResponse.createSuccess(result, "닉네임 중복 검사 성공(사용 가능)"));
        }
    }

    // 유저 닉네임 변경
    public ResponseEntity<ApiResponse<?>> updateUserNickname(String accessToken, String nickname){
        UserNicknameUpdateResponseDTO result = userService.updateUserNickname(accessToken, nickname);
        return ResponseEntity.status(200).body(ApiResponse.createSuccess(result, "유저 닉네임 변경 성공"));
    }
    
    // 유저 프로필 사진 변경
    public ResponseEntity<ApiResponse<?>> updateUserProfileImage(String accessToken, MultipartFile file) throws IOException {
        UserProfileImageUpdateResponseDTO result = userService.updateUserProfileImage(accessToken, file);

        return ResponseEntity.status(201).body(ApiResponse.createSuccess(result, "유저 프로필 사진 변경 성공"));
    }

    // 유저 정보 조회(이메일 검색)
    public ResponseEntity<ApiResponse<?>> findUserInfoToEmail(String accessToken, String email) {
        UserInviteInfoResponseDTO userInfoToEmail = userService.findUserInfoToEmail(email);

        return ResponseEntity.status(200).body(ApiResponse.createSuccess(userInfoToEmail, "이메일로 유저 조회 성공"));
    }

    // 유저 상태메시지 변경
    public ResponseEntity<ApiResponse<?>> changeStatusMessage(String accessToken, StatusMessageRequestDTO dto){
        UserStatusMessageUpdateResponseDTO responseDTO = userService.updateUserStatusMessage(accessToken, dto.getMessage());

        return ResponseEntity.status(200).body(ApiResponse.createSuccess(responseDTO,"유저 상태메시지 변경 성공"));
    }
}