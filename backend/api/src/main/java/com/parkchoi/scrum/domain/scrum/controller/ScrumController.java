package com.parkchoi.scrum.domain.scrum.controller;

import com.parkchoi.scrum.domain.scrum.dto.request.CreateScrumRequestDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumPageResponseDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomListResponseDTO;
import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.scrum.service.ScrumService;
import com.parkchoi.scrum.util.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "03.Scrum", description = "스크럼 API")
@Validated
public class ScrumController {

    private final ScrumService scrumService;

    // 스크럼 생성
    @Operation(summary = "스크럼 생성 API", description = "team_id와 스크럼 생성에 필요한 정보들을 받아서 스크럼을 생성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "스크럼 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없습니다.", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "쿠키가 존재하지 않습니다.", content = @Content)
    })
    @PostMapping("team/{team_id}/scrum")
    public ResponseEntity<ApiResponse<Void>> createScrum(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable(name = "team_id") Long teamId,
            @RequestBody @Valid CreateScrumRequestDTO dto
    ){
        scrumService.createScrum(accessToken, teamId, dto);

        return ResponseEntity.status(201).body(ApiResponse.createSuccessNoContent("스크럼 생성 성공"));
    }

    // 내가 속한 팀의 스크럼 방 조회
    @Operation(summary = "특정 팀 스크럼 조회 API", description = "현재 유저가 속한 팀의 모든 스크럼 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "특정 팀 스크럼 조회 성공",  content = @Content(schema = @Schema(implementation = ScrumRoomListResponseDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없습니다.", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "쿠키가 존재하지 않습니다.", content = @Content)
    })
    @GetMapping("team/{team_id}/scrums")
    public ResponseEntity<ApiResponse<ScrumRoomListResponseDTO>> findScrums(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable(name = "team_id") @NotNull Long teamId
    ){
        ScrumRoomListResponseDTO scrums = scrumService.findScrums(accessToken, teamId);

        return ResponseEntity.status(200).body(ApiResponse.createSuccess(scrums, "스크럼 조회 성공"));
    }

    // 스크럼 참여
    @Operation(summary = "스크럼 참여 API", description = "현재 유저가 특정 스크럼에 참여합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스크럼 참여 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없습니다.", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "쿠키가 존재하지 않습니다.", content = @Content)
    })
    @PostMapping("team/{team_id}/scrum/{scrum_id}/enter")
    public ResponseEntity<ApiResponse<Void>> enterScrum(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable(name = "team_id") @NotNull(message = "팀 아이디는 필수입니다.") Long teamId,
            @PathVariable(name = "scrum_id") @NotNull(message = "스크럼 아이디는 필수입니다.") Long scrumId
    ){
        scrumService.enterScrum(accessToken, teamId, scrumId);

        return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("스크럼 참여 성공"));
    }

    // 스크럼 삭제
    @Operation(summary = "스크럼 삭제 API", description = "현재 유저가 특정 스크럼을 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스크럼 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없습니다.", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "쿠키가 존재하지 않습니다.", content = @Content)
    })
    @PatchMapping("team/{team_id}/scrum/{scrum_id}/delete")
    public ResponseEntity<ApiResponse<Void>> removeScrum(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable(name = "team_id") @NotNull(message = "팀 아이디는 필수입니다.") Long teamId,
            @PathVariable(name = "scrum_id") @NotNull(message = "스크럼 아이디는 필수입니다.") Long scrumId
    ){
        scrumService.removeScrum(accessToken, teamId, scrumId);

        return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("스크럼 삭제 성공"));
    }

    // 스크럼 시작
    @Operation(summary = "스크럼 시작 API", description = "스크럼의 리더가 스크럼을 시작합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스크럼 시작 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없습니다.", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "쿠키가 존재하지 않습니다.", content = @Content)
    })
    @PatchMapping("team/{team_id}/scrum/{scrum_id}/start")
    public ResponseEntity<ApiResponse<Void>> startScrum(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable(name = "team_id") @NotNull(message = "팀 아이디는 필수입니다.")Long teamId,
            @PathVariable(name = "scrum_id") @NotNull(message = "스크럼 아이디는 필수입니다.")Long scrumId
    ){
        scrumService.startScrum(accessToken, teamId, scrumId);

        return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("스크럼을 시작합니다."));
    }

    // 스크럼 종료
    @Operation(summary = "스크럼 종료 API", description = "스크럼의 리더가 스크럼을 종료합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스크럼 종료 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없습니다.", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "쿠키가 존재하지 않습니다.", content = @Content)
    })
    @PatchMapping("team/{team_id}/scrum/{scrum_id}/end")
    public ResponseEntity<ApiResponse<Void>> endScrum(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @PathVariable(name = "team_id") @NotNull(message = "팀 아이디는 필수입니다.") Long teamId,
            @PathVariable(name = "scrum_id") @NotNull(message = "스크럼 아이디는 필수입니다.") Long scrumId
    ){
        scrumService.endScrum(accessToken, teamId, scrumId);

        return ResponseEntity.status(200).body(ApiResponse.createSuccessNoContent("스크럼을 종료합니다."));
    }

    // 스크럼 생성 가능 여부 확인
    @Operation(summary = "스크럼 생성 가능 여부 확인 API", description = "현재 유저가 스크럼 생성이 가능한지 확인합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "true = 스크럼 생성 가능, false = 스크럼 생성 불가능"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없습니다.", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "쿠키가 존재하지 않습니다.", content = @Content)
    })
    @GetMapping("scrum/creation-availability")
    public ResponseEntity<ApiResponse<Boolean>> checkScrumAvailability(
            @CookieValue(name = "accessToken", required = false) String accessToken){
        boolean result = scrumService.checkScrumAvailability(accessToken);

        return ResponseEntity.status(200).body(ApiResponse.createSuccess(result, "true = 스크럼 생성 가능, false = 스크럼 생성 불가"));
    }

    // 스크럼 검색(제목 or 리더)
    @Operation(summary = "스크럼 조건 검색 API", description = "입력한 스크럼 정보로 해당 스크럼을 검색합니다. ")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스크럼 검색 완료"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없습니다.", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "쿠키가 존재하지 않습니다.", content = @Content)
    })
    @GetMapping("team/{team_id}/scrum")
    public ResponseEntity<ApiResponse<?>> findScrumList(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "leaderName") String leaderName,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @PathVariable(name = "team_id") @NotNull(message = "팀 아이디는 필수입니다.") Long teamId){

        // 검색 조건이 둘 다 null인 경우
        if((name == null || name.trim().isEmpty()) && (leaderName == null || leaderName.trim().isEmpty())){
            return ResponseEntity.status(404).body(ApiResponse.createClientError("검색어를 필수로 입력해야 합니다."));
        }

        Pageable pageable = PageRequest.of(page, 6);
        Page<Scrum> scrums = scrumService.searchScrum(accessToken, name, leaderName, teamId, pageable);
        ScrumPageResponseDTO scrumPageResponseDTO = new ScrumPageResponseDTO(scrums);

        return ResponseEntity.status(200).body(ApiResponse.createSuccess(scrumPageResponseDTO, "검색 스크럼 목록"));
    }


}
