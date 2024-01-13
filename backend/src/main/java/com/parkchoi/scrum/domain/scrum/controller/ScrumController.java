package com.parkchoi.scrum.domain.scrum.controller;

import com.parkchoi.scrum.domain.scrum.dto.request.CreateScrumRequestDTO;
import com.parkchoi.scrum.domain.scrum.service.ScrumService;
import com.parkchoi.scrum.util.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class ScrumController {

    private final ScrumService scrumService;

    // 스크럼 생성
    @PostMapping("team/{team_id}/scrum")
    public ApiResponse<?> createScrum(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable(name = "team_id") Long teamId,
            @RequestBody CreateScrumRequestDTO dto
            ){
        scrumService.createScrum(accessToken, teamId, dto);

        return ApiResponse.createSuccessNoContent("스크럼 생성 성공");
    }
}
