package com.parkchoi.scrum.domain.user.controller;

import com.parkchoi.scrum.domain.user.dto.response.UserInviteInfoResponseDTO;
import com.parkchoi.scrum.domain.user.dto.response.UserLoginInfoResponseDTO;
import com.parkchoi.scrum.domain.user.service.UserService;
import com.parkchoi.scrum.util.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse<?> logout(@CookieValue(name = "accessToken", required = false) String accessToken, HttpServletResponse response){
        userService.logout(accessToken, response);

        return ApiResponse.createSuccessNoContent("로그아웃 성공");
    }

    // 유저 로그인 실행
    @Operation(summary = "유저 로그인 API", description = "AccessToken을 통해 로그인을 진행합니다.")
    @Parameter(name = "accessToken", description = "엑세스 토큰")
    @PostMapping("/user/login")
    public ApiResponse<?> login(@CookieValue(name = "accessToken", required = false) String accessToken) {
        UserLoginInfoResponseDTO userInfo = userService.getUserInfo(accessToken);

        return ApiResponse.createSuccess(userInfo, "로그인 성공");
    }

    // 닉네임 중복 검사
    @GetMapping("/user/{nickname}/existence")
    public ApiResponse<?> checkDuplicationNickname(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable("nickname") String nickname) {
        boolean result = userService.checkDuplicationNickname(accessToken, nickname);

        if (result) {
            return ApiResponse.createSuccess(result, "닉네임 중복 검사 성공(사용 불가)");
        } else {
            return ApiResponse.createSuccess(result, "닉네임 중복 검사 성공(사용 가능)");
        }
    }

    // 유저 정보 조회(이메일 검색)
    @GetMapping("/user/{email}/find")
    public ApiResponse<?> findUserInfoToEmail(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable("email") String email) {
        UserInviteInfoResponseDTO userInfoToEmail = userService.findUserInfoToEmail(accessToken, email);

        return ApiResponse.createSuccess(userInfoToEmail, "이메일로 유저 조회 성공");
    }

}
