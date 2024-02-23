package com.parkchoi.scrum.domain.scrum.service.impl;

import com.parkchoi.scrum.domain.exception.exception.ScrumException;
import com.parkchoi.scrum.domain.exception.info.ScrumExceptionInfo;
import com.parkchoi.scrum.domain.scrum.dto.request.CreateScrumRequestDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumPageResponseDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomListResponseDTO;
import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.scrum.entity.ScrumParticipant;
import com.parkchoi.scrum.domain.scrum.repository.scrum.ScrumRepository;
import com.parkchoi.scrum.domain.scrum.repository.scrumparticipant.ScrumParticipantRepository;
import com.parkchoi.scrum.domain.scrum.service.ScrumService;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.team.exception.NonParticipantUserException;
import com.parkchoi.scrum.domain.team.exception.TeamNotFoundException;
import com.parkchoi.scrum.domain.team.repository.InviteTeamListRepository;
import com.parkchoi.scrum.domain.team.repository.TeamRepository;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.util.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrumServiceImpl implements ScrumService {

    private final ScrumRepository scrumRepository;
    private final ScrumParticipantRepository scrumParticipantRepository;
    private final InviteTeamListRepository inviteTeamListRepository;
    private final TeamRepository teamRepository;
    private final SecurityContext securityContext;

    // 스크럼 생성
    @Override
    @Transactional
    public void createScrum(Long teamId, CreateScrumRequestDTO dto) {
        User user = securityContext.getUser();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("DB에서 " + teamId + "번 팀을 찾지 못했습니다."));

        try {
            // 스크럼 생성
            Scrum scrum = dto.toEntity(dto, user, team);

            scrumRepository.save(scrum);

            // 스크럼 참여자 정보 생성
            ScrumParticipant scrumParticipant = ScrumParticipant.builder()
                    .scrum(scrum)
                    .user(user).build();

            scrumParticipantRepository.save(scrumParticipant);
        } catch(Exception e) {
            throw new ScrumException(ScrumExceptionInfo.SCRUM_CREATION_FAIL, "스크럼 생성에 실패하였습니다.");
        }
    }

    // 스크럼 팀 조회
    @Override
    @Transactional(readOnly = true)
    public ScrumRoomListResponseDTO findScrums(Long teamId) {
        User user = securityContext.getUser();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("DB에서 " + teamId + "번 팀을 찾지 못했습니다."));

        inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException(teamId + "번 팀 초대 리스트에" + user.getId() + "번 유저가 존재 하지 않습니다."));

        List<Scrum> scrumList = scrumRepository.findActiveScrumsByTeam(team);

        List<ScrumRoomDTO> scrumRoomDTOList = scrumList.stream()
                .map(ScrumRoomDTO::fromEntity)
                .collect(Collectors.toList());

        return new ScrumRoomListResponseDTO(scrumRoomDTOList);
    }


    // 스크럼 참여
    @Override
    @Transactional
    public void enterScrum(Long teamId, Long scrumId){
        User user = securityContext.getUser();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("DB에서 " + teamId + "번 팀을 찾지 못했습니다."));

        inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException(teamId + "번 팀 초대 리스트에 " + user.getId() + "번 유저가 존재 하지 않습니다."));

        Scrum scrum = scrumRepository.findActiveScrumByScrumId(scrumId)
                .orElseThrow(() -> new ScrumException(ScrumExceptionInfo.SCRUM_NOT_FOUND, "DB에서 " + scrumId +" 번 스크럼을 찾지 못했습니다.(삭제 됐거나 종료 됐거나 스크럼이 없음)"));

        // 아직 참여하지 않은 스크럼이면
        if(!scrumParticipantRepository.existsEnterScrum(user, scrum)){
            // 멤버가 꽉찼으면
            if(scrum.getCurrentMember() == scrum.getMaxMember()){
                throw new ScrumException(ScrumExceptionInfo.SCRUM_MEMBER_MAXED, "현재 " + scrumId + "번 스크럼 인원이 최대입니다.");
            }
            // 참여 생성
            ScrumParticipant scrumParticipant = ScrumParticipant.builder()
                    .scrum(scrum)
                    .user(user).build();
            scrumParticipantRepository.save(scrumParticipant);
            // 현재 인원 증가
            scrum.plusCurrentMember();
        }else{
            throw new ScrumException(ScrumExceptionInfo.SCRUM_ALREADY_ENTERED, user.getId() + "번 유저는 이미 " + scrumId + "번 스크럼에 참여중 입니다.");
        }
        // 나중에 화면에 필요한 정보 리턴
    }

    // 스크럼 삭제
    @Override
    @Transactional
    public void removeScrum(Long teamId, Long scrumId) {
        User user = securityContext.getUser();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("DB에서 " + teamId + "번 팀을 찾지 못했습니다."));

        inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException(teamId + "번 팀 초대 리스트에" + user.getId() + "번 유저가 존재 하지 않습니다."));

        Scrum scrum = scrumRepository.findActiveScrumByScrumId(scrumId)
                .orElseThrow(() -> new ScrumException(ScrumExceptionInfo.SCRUM_NOT_FOUND, "DB에서 "+ scrumId+"번 스크럼을 찾지 못했습니다.(삭제 됐거나 종료 됐거나 스크럼이 없음)"));

        if (!scrum.getUser().equals(user)) {
            throw new ScrumException(ScrumExceptionInfo.NOT_SCRUM_LEADER, user.getId() + "번 유저는 " + scrumId + "번 스크럼의 리더가 아니라 삭제 불가능 합니다.");
        }

        // 스크럼 삭제 진행
        scrum.deleteScrum();
    }

    // 스크럼 시작
    @Override
    @Transactional
    public void startScrum(Long teamId, Long scrumId){
        User user = securityContext.getUser();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("DB에서 " + teamId + "번 팀을 찾지 못했습니다."));

        inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException(teamId + "번 팀 초대 리스트에" + user.getId() + "번 유저가 존재 하지 않습니다."));

        Scrum scrum = scrumRepository.findActiveScrumByScrumId(scrumId)
                .orElseThrow(() -> new ScrumException(ScrumExceptionInfo.SCRUM_NOT_FOUND, "DB에서 "+ scrumId+"번 스크럼을 찾지 못했습니다.(삭제 됐거나 종료 됐거나 스크럼이 없음)"));

        if (!scrum.getUser().equals(user)) {
            throw new ScrumException(ScrumExceptionInfo.NOT_SCRUM_LEADER, "번 유저는 " + scrumId + "번 스크럼의 리더가 아니라 시작 불가능 합니다.");
        }

        // 이미 스크럼이 시작했다면
        if (scrum.getIsStart()){
            throw new ScrumException(ScrumExceptionInfo.SCRUM_ALREADY_STARTED, scrumId + "번 스크럼은 이미 시작된 스크럼 입니다.");
        }

        // 시작 상태 변경과 시간 추가
        scrum.startScrum();
    }

    // 스크럼 종료
    @Override
    @Transactional
    public void endScrum(Long teamId, Long scrumId){
        User user = securityContext.getUser();
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("DB에서 " + teamId + "번 팀을 찾지 못했습니다."));

        inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException(teamId + "번 팀 초대 리스트에" + user.getId() + "번 유저가 존재 하지 않습니다."));

        Scrum scrum = scrumRepository.findActiveScrumByScrumId(scrumId)
                .orElseThrow(() -> new ScrumException(ScrumExceptionInfo.SCRUM_NOT_FOUND, "DB에서 "+ scrumId+"번 스크럼을 찾지 못했습니다.(삭제 됐거나 종료 됐거나 스크럼이 없음)"));

        if (!scrum.getUser().equals(user)) {
            throw new ScrumException(ScrumExceptionInfo.NOT_SCRUM_LEADER, "번 유저는 " + scrumId + "번 스크럼의 리더가 아니라 종료 불가능 합니다.");
        }

        // 아직 스크럼이 시작하지 않았으면
        if (!scrum.getIsStart()){
            throw new ScrumException(ScrumExceptionInfo.SCRUM_NOT_STARTED, scrumId + "번 스크럼은 아직 시작 하지 않았습니다.");
        }

        scrum.endScrum();
    }

    // 스크럼 생성 가능 여부 확인
    @Override
    public boolean checkScrumAvailability(){
        User user = securityContext.getUser();

        boolean result = scrumRepository.existsActiveScrumByUser(user);
        return !result;
    }

    // 스크럼 검색
    @Override
    @Transactional(readOnly = true)
    public ScrumPageResponseDTO searchScrum(String type, String key, Long teamId, Pageable pageable){
        User user = securityContext.getUser();
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("DB에서 " + teamId + "번 팀을 찾지 못했습니다."));

        inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException(teamId + "번 팀 초대 리스트에" + user.getId() + "번 유저가 존재 하지 않습니다."));

        Page<Scrum> scrums = scrumRepository.searchScrumWithPagination(type, key, pageable);

        return new ScrumPageResponseDTO(scrums);
    }
}
