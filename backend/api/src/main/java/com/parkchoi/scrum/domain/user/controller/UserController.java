package com.parkchoi.scrum.domain.user.controller;

import com.parkchoi.scrum.domain.user.dto.request.StatusMessageRequestDTO;
import com.parkchoi.scrum.domain.user.dto.response.*;
import com.parkchoi.scrum.domain.user.service.UserService;
import com.parkchoi.scrum.util.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "01.User", description = "유저 API")
@Validated
public class UserController{

    private final UserService userService;

    // 유저 로그아웃
    @Operation(summary = "유저 로그아웃 API", description = "모든 쿠키를 삭제합니다. isOnline = false")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저 없음", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "쿠키가 존재하지 않습니다.", content = @Content)
    })
    @PatchMapping("/user/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@CookieValue(name = "accessToken", required = false) String accessToken, HttpServletResponse response){
        userService.logout(accessToken, response);

        return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("로그아웃 성공"));
    }

    // 유저 로그인 실행
    @Operation(summary = "유저 로그인 API", description = "AccessToken을 통해 로그인을 진행합니다.")
    @Parameter(name = "accessToken", description = "엑세스 토큰")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "로그인 성공", content = @Content(schema = @Schema(implementation = UserLoginInfoResponseDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저 없음", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "쿠키가 존재하지 않습니다.", content = @Content)
    })
    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse<UserLoginInfoResponseDTO>> login(@CookieValue(name = "accessToken", required = false) String accessToken) {
        UserLoginInfoResponseDTO userInfo = userService.getUserInfo(accessToken);

        return ResponseEntity.status(201).body(ApiResponse.createSuccess(userInfo, "로그인 성공"));
    }

    // 닉네임 중복 검사
    @Operation(summary = "닉네임 중복 검사 API", description = "사용 불가 닉네임 = true, 사용 가능 닉네임 = false로 응답합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "닉네임 중복 검사 성공(사용 불가 or 사용 가능)")
    })
    @GetMapping("/user/{nickname}/existence")
    public ResponseEntity<ApiResponse<Boolean>> checkDuplicationNickname(
            @PathVariable("nickname")
            @Size(max = 10, message = "닉네임은 최대 10자까지 가능합니다.")
            @NotBlank(message = "닉네임을 입력해주세요.")
            @Pattern(regexp = "^[가-힣A-Za-z]+$", message = "닉네임은 한글, 영어만 가능합니다.")
            @Schema(description = "닉네임(최대 10글자, 한글 및 영어만 가능)")
            String nickname) {
        boolean result = userService.checkDuplicationNickname(nickname);

        if (result) {
            return ResponseEntity.status(200).body(ApiResponse.createSuccess(true, "닉네임 중복 검사 성공(사용 불가)"));
        } else {
            return ResponseEntity.status(200).body(ApiResponse.createSuccess(false, "닉네임 중복 검사 성공(사용 가능)"));
        }
    }

    // 유저 닉네임 변경
    @Operation(summary = "유저 닉네임 변경 API", description = "파라미터로 넣은 nickname을 받아서 닉네임 변경 진행합니다. 최대 10글자 가능합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 닉네임 변경 성공", content = @Content(schema = @Schema(implementation = UserNicknameUpdateResponseDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저 없음", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "쿠키가 존재하지 않습니다.", content = @Content)
    })
    @PatchMapping("/user/nickname")
    public ResponseEntity<ApiResponse<UserNicknameUpdateResponseDTO>> updateUserNickname(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @Size(max = 10, message = "닉네임은 최대 10자까지 가능합니다.")
            @NotBlank(message = "닉네임을 입력해주세요.")
            @Pattern(regexp = "^[가-힣A-Za-z]+$", message = "닉네임은 한글, 영어만 가능합니다.")
            @Schema(description = "닉네임(최대 10글자, 한글 및 영어만 가능)")
            @RequestParam(name = "nickname")
            String nickname){
        UserNicknameUpdateResponseDTO result = userService.updateUserNickname(accessToken, nickname);
        return ResponseEntity.status(200).body(ApiResponse.createSuccess(result, "유저 닉네임 변경 성공"));
    }

    // 유저 프로필 사진 변경
    @Operation(summary = "유저 프로필 사진 변경 API", description = "파라미터로 넣은 file을 받아서 닉네임 변경 진행합니다.\n이미지 확장자는 jpg, jpeg, png로 제한했습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 프로필 사진 변경 성공", content = @Content(schema = @Schema(implementation = UserProfileImageUpdateResponseDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저 없음", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "쿠키가 존재하지 않습니다.", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "415", description = "이미지 확장자가 아닙니다.", content = @Content)
    })
    @PatchMapping(value = "/user/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UserProfileImageUpdateResponseDTO>> updateUserProfileImage(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @RequestParam(name = "file") @NotNull
            @Schema(description = "이미지 사진(jpg, jpeg, png)")
            MultipartFile file) throws IOException {
        UserProfileImageUpdateResponseDTO result = userService.updateUserProfileImage(accessToken, file);

        return ResponseEntity.status(201).body(ApiResponse.createSuccess(result, "유저 프로필 사진 변경 성공"));
    }

    // 유저 정보 조회(이메일 검색)
    @Operation(summary = "유저 정보 조회(이메일 검색) API", description = "이메일을 통해 해당 유저의 정보를 조회합니다.(팀 생성 -> 유저 초대시에 사용)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이메일로 유저 조회 성공", content = @Content(schema = @Schema(implementation = UserInviteInfoResponseDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "쿠키가 존재하지 않습니다.", content = @Content)
    })
    @GetMapping("/user/{email}/find")
    public ResponseEntity<ApiResponse<UserInviteInfoResponseDTO>> findUserInfoToEmail(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable("email")
            @NotNull(message = "이메일은 필수입니다.")
            @Email(message = "이메일 형식이 아닙니다.")
            @Schema(description = "이메일") String email) {
        UserInviteInfoResponseDTO userInfoToEmail = userService.findUserInfoToEmail(email);

        return ResponseEntity.status(200).body(ApiResponse.createSuccess(userInfoToEmail, "이메일로 유저 조회 성공"));
    }

    // 유저 상태메시지 변경
    @Operation(summary = "유저 상태메시지 변경 API", description = "유저의 상태메시지를 변경합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "유저 상태메시지 변경 성공", content = @Content(schema = @Schema(implementation = UserStatusMessageUpdateResponseDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저 없음", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "쿠키가 존재하지 않습니다.", content = @Content)
    })
    @PatchMapping("/user/status-message")
    public ResponseEntity<ApiResponse<UserStatusMessageUpdateResponseDTO>> changeStatusMessage(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @RequestBody @Valid
            StatusMessageRequestDTO dto){
        UserStatusMessageUpdateResponseDTO responseDTO = userService.updateUserStatusMessage(accessToken, dto.getMessage());

        return ResponseEntity.status(200).body(ApiResponse.createSuccess(responseDTO,"유저 상태메시지 변경 성공"));
    }
}