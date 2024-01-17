package com.parkchoi.scrum.domain.user.controller;

import com.parkchoi.scrum.domain.user.dto.response.UserInviteInfoResponseDTO;
import com.parkchoi.scrum.domain.user.dto.response.UserLoginInfoResponseDTO;
import com.parkchoi.scrum.domain.user.dto.response.UserNicknameUpdateResponseDTO;
import com.parkchoi.scrum.domain.user.dto.response.UserProfileImageUpdateResponseDTO;
import com.parkchoi.scrum.domain.user.service.UserService;
import com.parkchoi.scrum.util.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "01.User")
public class UserController {

    private final UserService userService;

    // 유저 로그아웃
    @Operation(summary = "유저 로그아웃 API", description = "모든 쿠키를 삭제합니다. isOnline = false")
    @PatchMapping("/user/logout")
    public ResponseEntity<ApiResponse<?>> logout(@CookieValue(name = "accessToken", required = false) String accessToken, HttpServletResponse response){
        userService.logout(accessToken, response);

        return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("로그아웃 성공"));
    }

    // 유저 로그인 실행
    @Operation(summary = "유저 로그인 API", description = "AccessToken을 통해 로그인을 진행합니다.")
    @Parameter(name = "accessToken", description = "엑세스 토큰")
    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse<?>> login(@CookieValue(name = "accessToken", required = false) String accessToken) {
        UserLoginInfoResponseDTO userInfo = userService.getUserInfo(accessToken);

        return ResponseEntity.status(201).body(ApiResponse.createSuccess(userInfo, "로그인 성공"));
    }

    // 닉네임 중복 검사
    @Operation(summary = "닉네임 중복 검사 API", description = "사용 불가 닉네임 = true, 사용 가능 닉네임 = false로 응답합니다.")
    @GetMapping("/user/{nickname}/existence")
    public ResponseEntity<ApiResponse<?>> checkDuplicationNickname(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable("nickname") String nickname) {
        boolean result = userService.checkDuplicationNickname(accessToken, nickname);

        if (result) {
            return ResponseEntity.status(200).body(ApiResponse.createSuccess(result, "닉네임 중복 검사 성공(사용 불가)"));
        } else {
            return ResponseEntity.status(200).body(ApiResponse.createSuccess(result, "닉네임 중복 검사 성공(사용 가능)"));
        }
    }

    // 유저 닉네임 변경
    @Operation(summary = "유저 닉네임 변경 API", description = "파라미터로 넣은 nickname을 받아서 닉네임 변경 진행합니다.")
    @PatchMapping("/user/nickname")
    public ResponseEntity<ApiResponse<?>> updateUserNickname(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @RequestParam String nickname){
        UserNicknameUpdateResponseDTO result = userService.updateUserNickname(accessToken, nickname);
        return ResponseEntity.status(201).body(ApiResponse.createSuccess(result, "유저 닉네임 변경 성공"));
    }
    
    // 유저 프로필 사진 변경
    @Operation(summary = "유저 프로필 사진 변경 API", description = "파라미터로 넣은 file을 받아서 닉네임 변경 진행합니다.\n이미지 확장자는 jpg, jpeg, png로 제한했습니다.")
    @PatchMapping(value = "/user/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> updateUserProfileImage(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @RequestParam(name = "file") MultipartFile file) throws IOException {
        UserProfileImageUpdateResponseDTO result = userService.updateUserProfileImage(accessToken, file);

        return ResponseEntity.status(201).body(ApiResponse.createSuccess(result, "유저 프로필 사진 변경 성공"));
    }

    // 유저 정보 조회(이메일 검색)
    @Operation(summary = "유저 정보 조회(이메일 검색) API", description = "이메일을 통해 해당 유저의 정보를 조회합니다.(팀 생성 -> 유저 초대시에 사용)")
    @GetMapping("/user/{email}/find")
    public ResponseEntity<ApiResponse<?>> findUserInfoToEmail(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable("email") String email) {
        UserInviteInfoResponseDTO userInfoToEmail = userService.findUserInfoToEmail(accessToken, email);

        return ResponseEntity.status(200).body(ApiResponse.createSuccess(userInfoToEmail, "이메일로 유저 조회 성공"));
    }

}
