package com.parkchoi.scrum.domain.user.controller;

import com.parkchoi.scrum.domain.user.dto.response.UserInfoResponseDTO;
import com.parkchoi.scrum.domain.user.service.UserService;
import com.parkchoi.scrum.util.api.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/userinfo")
    public ApiResponse<?> login(HttpServletRequest request){
        UserInfoResponseDTO userInfo = userService.getUserInfo(request);

        return ApiResponse.createSuccess(userInfo, "로그인 성공");
    }

}
