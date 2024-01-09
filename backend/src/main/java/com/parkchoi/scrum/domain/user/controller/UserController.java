package com.parkchoi.scrum.domain.user.controller;

import com.parkchoi.scrum.domain.user.dto.response.UserInfoResponseDTO;
import com.parkchoi.scrum.domain.user.service.UserService;
import com.parkchoi.scrum.util.api.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // 유저 로그인 실행
    @PostMapping("/login")
    public ApiResponse<?> login(HttpServletRequest request){
        UserInfoResponseDTO userInfo = userService.getUserInfo(request);

        return ApiResponse.createSuccess(userInfo, "로그인 성공");
    }

    // 닉네임 중복 검사
    @GetMapping("/user/{nickname}")
    public ApiResponse<?> checkDuplicationNickname(HttpServletRequest request, @PathVariable String nickname){
        boolean result = userService.checkDuplicationNickname(request, nickname);
        
        if(result){
            return ApiResponse.createSuccess(result, "닉네임 중복 검사 성공(사용 불가)");
        }else{
            return ApiResponse.createSuccess(result, "닉네임 중복 검사 성공(사용 가능)");
        }
    }

}
