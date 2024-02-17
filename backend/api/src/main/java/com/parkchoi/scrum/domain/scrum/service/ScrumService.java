package com.parkchoi.scrum.domain.scrum.service;

import com.parkchoi.scrum.domain.scrum.dto.request.CreateScrumRequestDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumPageResponseDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomListResponseDTO;
import org.springframework.data.domain.Pageable;

public interface ScrumService {
    // 스크럼 생성
    void createScrum(String accessToken, Long teamId, CreateScrumRequestDTO dto);
    // 스크럼 조회
    ScrumRoomListResponseDTO findScrums(String accessToken, Long teamId);
    // 스크럼 참여
    void enterScrum(String accessToken, Long teamId, Long scrumId);
    // 스크럼 삭제
    void removeScrum(String accessToken, Long teamId, Long scrumId);
    // 스크럼 시작
    void startScrum(String accessToken, Long teamId, Long scrumId);
    // 스크럼 종료
    void endScrum(String accessToken, Long teamId, Long scrumId);
    // 스크럼 생성 가능 여부 확인
    boolean checkScrumAvailability(String accessToken);
    // 스크럼 검색
    ScrumPageResponseDTO searchScrum(String accessToken, String type, String key, Long teamId, Pageable pageable);

}
