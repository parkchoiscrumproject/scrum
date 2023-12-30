package com.parkchoi.scrum.domain.user.controller;

import com.parkchoi.scrum.domain.user.dto.request.AccessTokenRequestDTO;
import com.parkchoi.scrum.domain.user.service.UserService;
import com.parkchoi.scrum.util.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody AccessTokenRequestDTO accessTokenRequestDTO){
        userService.login(accessTokenRequestDTO);

        return ApiResponse.createSuccessNoContent("로그인 성공");
    }

}
