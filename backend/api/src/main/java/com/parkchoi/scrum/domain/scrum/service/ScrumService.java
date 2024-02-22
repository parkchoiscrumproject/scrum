package com.parkchoi.scrum.domain.scrum.service;

import com.parkchoi.scrum.domain.scrum.dto.request.CreateScrumRequestDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumPageResponseDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomListResponseDTO;
import org.springframework.data.domain.Pageable;

public interface ScrumService {
    // 스크럼 생성
    void createScrum(Long teamId, CreateScrumRequestDTO dto);
    // 스크럼 조회
    ScrumRoomListResponseDTO findScrums(Long teamId);
    // 스크럼 참여
    void enterScrum(Long teamId, Long scrumId);
    // 스크럼 삭제
    void removeScrum(Long teamId, Long scrumId);
    // 스크럼 시작
    void startScrum(Long teamId, Long scrumId);
    // 스크럼 종료
    void endScrum(Long teamId, Long scrumId);
    // 스크럼 생성 가능 여부 확인
    boolean checkScrumAvailability();
    // 스크럼 검색
    ScrumPageResponseDTO searchScrum(String type, String key, Long teamId, Pageable pageable);

}
