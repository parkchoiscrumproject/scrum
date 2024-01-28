package com.parkchoi.scrum.domain.team.controller;

import com.parkchoi.scrum.domain.team.dto.request.CreateTeamRequestDTO;
import com.parkchoi.scrum.domain.team.dto.request.TeamInvitationRequestDTO;
import com.parkchoi.scrum.domain.team.dto.response.CreateTeamResponseDTO;
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
    @Operation(summary = "팀 생성 API", description = "파일(사진)과 팀 정보 초대 리스트를 입력하여 진행합니다.")
    @PostMapping("/team")
    public ResponseEntity<ApiResponse<?>> createTeam(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "dto") CreateTeamRequestDTO dto) throws IOException {

        CreateTeamResponseDTO result = teamService.createTeam(accessToken, file, dto);

        return ResponseEntity.status(201).body(ApiResponse.createSuccess(result, "팀 생성 성공"));
    }


    // 팀 삭제
    @Operation(summary = "팀 삭제 API", description = "파라미터로 넣은 team_id를 받아서 팀 삭제를 진행합니다.")
    @DeleteMapping("/team/{team_id}")
    public ResponseEntity<ApiResponse<?>> deleteTeam(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable Long team_id) {

        teamService.removeTeam(accessToken, team_id);

        return ResponseEntity.status(204).body(ApiResponse.createSuccessNoContent("팀 삭제 성공"));
    }



    // 팀원 초대
    @Operation(summary = "팀원 초대 API", description = "파라미터로 넣은 team_id를 받아서 팀원 초대를 진행합니다.")
    @PostMapping("/team/{team_id}/invite")
    public ResponseEntity<ApiResponse<?>> inviteTeamMember(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable(name = "team_id") Long teamId,
            @RequestBody TeamInvitationRequestDTO dto){

        teamService.inviteTeamMember(accessToken,teamId,dto);

        return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("팀원 초대를 성공하였습니다."));
    }

    //팀 초대 승낙
    @Operation(summary = "팀원 초대 승낙 API", description = "파라미터로 넣은 team_id와 invite_id를 받아서 팀 초대를 승낙합니다.")
    @PatchMapping("/team/{team_id}/invite/{invite_id}")
    public ResponseEntity<ApiResponse<?>> acceptTeamInvite(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable(name = "team_id") Long teamId,
            @PathVariable(name = "invite_id") Long inviteId){

        teamService.acceptTeamInvite(accessToken,teamId,inviteId);

        return  ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("팀 초대 승낙을 성공하였습니다."));
    }


    // 팀 초대 거절
    @Operation(summary = "팀 초대 거절 API", description = "파라미터로 넣은 team_id와 invite_id를 받아서 팀 초대를 거절합니다.")
    @DeleteMapping("/team/{team_id}/invite/{invite_id}")
    public ResponseEntity<ApiResponse<?>> refuseTeamInvite(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable(name = "team_id") Long teamId,
            @PathVariable(name = "invite_id") Long inviteId){

        teamService.refuseTeamInvite(accessToken,teamId,inviteId);

        return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("팀 초대 거절을 성공하였습니다."));
    }

    // 나의 팀 목록 조회










}










