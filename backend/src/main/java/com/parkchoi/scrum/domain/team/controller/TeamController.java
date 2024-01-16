package com.parkchoi.scrum.domain.team.controller;

import com.parkchoi.scrum.domain.team.dto.request.CreateTeamRequestDTO;
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
            @CookieValue(name = "accessToken",required = false) String accessToken,
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "dto") CreateTeamRequestDTO dto) throws IOException {


        CreateTeamResponseDTO result = teamService.createTeam(accessToken, file, dto);

     return ResponseEntity.status(201).body(ApiResponse.createSuccess(result,"팀 생성 성공"));
    }




    // 팀 삭제
//    @Operation(summary = "팀 삭제 API", description = "파라미터로 넣은 team_id를 받아서 팀 삭제를 진행합니다.")
//    @DeleteMapping("/team/{team_id}")
//    public ResponseEntity<ApiResponse<?>> deleteTeam(
//            @CookieValue(name = "accessToken", required = false) String accessToken,
//            @RequestParam String team_id){
//
//
//
//
//        return ResponseEntity.status(204).body(ApiResponse.createSuccessNoContent())
//    }
}
