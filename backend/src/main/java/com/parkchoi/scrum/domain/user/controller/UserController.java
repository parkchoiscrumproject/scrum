package com.parkchoi.scrum.domain.user.controller;

import com.parkchoi.scrum.domain.user.service.UserService;
import com.parkchoi.scrum.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/regist")
    public ApiResponse<> regist(){
        userService
    }
}
