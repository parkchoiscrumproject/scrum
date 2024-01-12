package com.parkchoi.scrum.domain.jwt.controller;

import com.parkchoi.scrum.domain.jwt.dto.request.JwtCreateRequestDTO;
import com.parkchoi.scrum.util.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class JwtController {

//    @PostMapping("jwt")
//    public ApiResponse<?> createAccessTokenTest(@RequestBody JwtCreateRequestDTO dto){
//
//    }

}
