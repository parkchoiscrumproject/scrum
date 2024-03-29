package com.parkchoi.scrum.domain.team.controller;

import com.parkchoi.scrum.domain.team.dto.request.CreateTeamRequestDTO;
import com.parkchoi.scrum.domain.team.dto.request.KickTeamRequestDTO;
import com.parkchoi.scrum.domain.team.dto.request.TeamInvitationRequestDTO;
import com.parkchoi.scrum.domain.team.dto.response.CreateTeamResponseDTO;
import com.parkchoi.scrum.domain.team.dto.response.TeamListResponseDTO;
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
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "dto") CreateTeamRequestDTO dto) throws IOException {

        CreateTeamResponseDTO result = teamService.createTeam(file, dto);

        return ResponseEntity.status(201).body(ApiResponse.createSuccess(result, "팀 생성 성공"));
    }


    // 팀 삭제
    @Operation(summary = "팀 삭제 API", description = "파라미터로 넣은 team_id를 받아서 팀 삭제를 진행합니다.")
    @DeleteMapping("/team/{team_id}")
    public ResponseEntity<ApiResponse<?>> deleteTeam(
            @PathVariable Long team_id) {

        teamService.removeTeam(team_id);

        return ResponseEntity.status(204).body(ApiResponse.createSuccessNoContent("팀 삭제 성공"));
    }



    // 팀원 초대
    @Operation(summary = "팀원 초대 API", description = "파라미터로 넣은 team_id를 받아서 팀원 초대를 진행합니다.")
    @PostMapping("/team/{team_id}/invite")
    public ResponseEntity<ApiResponse<?>> inviteTeamMember(
            @PathVariable(name = "team_id") Long teamId,
            @RequestBody TeamInvitationRequestDTO dto){

        teamService.inviteTeamMember(teamId,dto);

        return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("팀원 초대를 성공하였습니다."));
    }

    //팀 초대 승낙
    @Operation(summary = "팀원 초대 승낙 API", description = "파라미터로 넣은 team_id와 invite_id를 받아서 팀 초대를 승낙합니다.")
    @PatchMapping("/team/{team_id}/invite/{invite_id}")
    public ResponseEntity<ApiResponse<?>> acceptTeamInvite(
            @PathVariable(name = "team_id") Long teamId,
            @PathVariable(name = "invite_id") Long inviteId){

        teamService.acceptTeamInvite(teamId,inviteId);

        return  ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("팀 초대 승낙을 성공하였습니다."));
    }


    // 팀 초대 거절
    @Operation(summary = "팀 초대 거절 API", description = "파라미터로 넣은 team_id와 invite_id를 받아서 팀 초대를 거절합니다.")
    @DeleteMapping("/team/{team_id}/invite/{invite_id}")
    public ResponseEntity<ApiResponse<?>> refuseTeamInvite(
            @PathVariable(name = "team_id") Long teamId,
            @PathVariable(name = "invite_id") Long inviteId){

        teamService.refuseTeamInvite(teamId,inviteId);

        return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("팀 초대 거절을 성공하였습니다."));
    }

    // 나의 팀 목록 조회
    @Operation(summary = "팀 목록 조회 API", description = "현재 유저가 참여하고 있는 팀의 목록을 조회합니다.")
    @GetMapping("/my-teams")
    public ResponseEntity<ApiResponse<?>> findMyTeams(
    ){
        TeamListResponseDTO teams = teamService.findMyTeams();

        return ResponseEntity.status(200).body(ApiResponse.createSuccess(teams,"팀 조회 성공"));
    }


    // 팀원 목록 조회
//    @Operation(summary = "팀원 목록 조회 API", description = "파라미터로 넣은 team_id를 받아 해당 팀의 멤버들을 조회합니다.")
//    @GetMapping("/team/{team_id}/members")
//    public ResponseEntity<ApiResponse<?>> findTeamMembers(
//            @CookieValue(name = "accessToken", required = false) String accessToken,
//            @PathVariable(name = "team_id") Long teamId){
//
//        MemberListResponseDTO members = teamService.findTeamMembers(accessToken,teamId);
//
//        return ResponseEntity.status(200).body(ApiResponse.createSuccess(members,"팀원 목록 조회 성공"));
//
//    }


    // 팀 나가기
    @Operation(summary = "팀 나가기 API", description = "파라미터로 넣은 team_id를 받아 해당 팀을 나갑니다.")
    @DeleteMapping("/team/{team_id}/leave")
    public ResponseEntity<ApiResponse<?>> leaveTeam(
            @PathVariable(name = "team_id") Long teamId){

        teamService.leaveTeam(teamId);

        return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("팀 나가기 성공"));
    }

    // 팀원 추방
    @Operation(summary = "팀원 추방 API", description = "파라미터로 넣은 team_id를 받아 해당 팀원을 추방시킵니다.")
    @DeleteMapping("/team/{team_id}/kick")
    public ResponseEntity<ApiResponse<?>> kickTeam(
            @PathVariable(name = "team_id") Long teamId,
            @RequestBody KickTeamRequestDTO dto
            ){

        teamService.kickTeam(teamId,dto);

        return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("팀 추방 성공"));
    }






}










