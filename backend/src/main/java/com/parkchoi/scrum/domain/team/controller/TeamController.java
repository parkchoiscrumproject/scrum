package com.parkchoi.scrum.domain.team.controller;

import com.parkchoi.scrum.domain.team.dto.request.CreateTeamRequestDTO;
import com.parkchoi.scrum.domain.team.service.TeamService;
import com.parkchoi.scrum.util.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor //생성자 주입
@Slf4j //로그를 쉽게 찍어 볼 수 있는 어노테이션
@Tag(name = "02.Team")
public class TeamController {

    private final TeamService teamService;

    // 팀 생성
    @Operation(summary = "팀 생성 API")
    @PostMapping("team")
    public ResponseEntity<ApiResponse<?>> createTeam(
            @CookieValue(name = "accessToken",required = false) String accessToken,
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "dto") CreateTeamRequestDTO dto) throws IOException {

        teamService.createTeam(accessToken, file, dto);

     return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("성공"));
    }
}
