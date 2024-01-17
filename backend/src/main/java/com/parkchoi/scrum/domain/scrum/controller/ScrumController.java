package com.parkchoi.scrum.domain.scrum.controller;

import com.parkchoi.scrum.domain.scrum.dto.request.CreateScrumRequestDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomListResponseDTO;
import com.parkchoi.scrum.domain.scrum.service.ScrumService;
import com.parkchoi.scrum.util.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "03.Scrum")
public class ScrumController {

    private final ScrumService scrumService;

    // 스크럼 생성
    @Operation(summary = "스크럼 생성 API", description = "team_id와 스크럼 생성에 필요한 정보들을 받아서 스크럼을 생성합니다.")
    @PostMapping("team/{team_id}/scrum")
    public ResponseEntity<ApiResponse<?>> createScrum(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable(name = "team_id") Long teamId,
            @RequestBody CreateScrumRequestDTO dto
            ){
        scrumService.createScrum(accessToken, teamId, dto);

        return ResponseEntity.status(201).body(ApiResponse.createSuccessNoContent("스크럼 생성 성공"));
    }

    // 내가 속한 팀의 스크럼 방 조회
    @Operation(summary = "특정 팀 스크럼 조회 API", description = "현재 유저가 속한 팀의 모든 스크럼 목록을 조회합니다.")
    @GetMapping("team/{team_id}/scrums")
    public ResponseEntity<ApiResponse<?>> findScrums(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable(name = "team_id") Long teamId
    ){
        ScrumRoomListResponseDTO scrums = scrumService.findScrums(accessToken, teamId);

        return ResponseEntity.status(200).body(ApiResponse.createSuccess(scrums, "스크럼 조회 성공"));
    }

    // 스크럼 참여
    @Operation(summary = "스크럼 참여 API", description = "현재 유저가 특정 스크럼에 참여합니다.")
    @PostMapping("team/{team_id}/scrum/{scrum_id}")
    public ResponseEntity<ApiResponse<?>> enterScrum(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable(name = "team_id") Long teamId,
            @PathVariable(name = "scrum_id") Long scrumId
    ){
        scrumService.enterScrum(accessToken, teamId, scrumId);

        return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("스크럼 참여 성공"));
    }
}
