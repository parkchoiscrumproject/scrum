package com.parkchoi.scrum.domain.team.controller;

import com.parkchoi.scrum.domain.team.dto.request.CreateTeamRequestDTO;
import com.parkchoi.scrum.domain.team.service.TeamService;
import com.parkchoi.scrum.util.api.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor //생성자 주입
@Slf4j //로그를 쉽게 찍어 볼 수 있는 어노테이션
@Tag(name = "02.Team")
public class TeamController {

    private final TeamService teamService;


    // 팀 생성
//    @PostMapping("team/create")
//    public ApiResponse<?> createTeam(
//            @CookieValue(name = "accessToken",required = false) String accessToken,
//            @RequestBody CreateTeamRequestDTO dto){
//     boolean result = teamService.createTeam(accessToken, dto);
//
//     return ApiResponse.createSuccess(result, "");
//    }
}
