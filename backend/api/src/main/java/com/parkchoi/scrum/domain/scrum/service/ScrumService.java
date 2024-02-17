package com.parkchoi.scrum.domain.scrum.service;

import com.parkchoi.scrum.domain.scrum.dto.request.CreateScrumRequestDTO;
import com.parkchoi.scrum.domain.scrum.dto.request.ScrumSearchCondition;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumPageResponseDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomListResponseDTO;
import com.parkchoi.scrum.domain.scrum.entity.QScrum;
import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.scrum.entity.ScrumParticipant;
import com.parkchoi.scrum.domain.scrum.exception.*;
import com.parkchoi.scrum.domain.scrum.repository.ScrumParticipantRepository;
import com.parkchoi.scrum.domain.scrum.repository.ScrumRepository;
import com.parkchoi.scrum.domain.team.entity.InviteTeamList;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.team.exception.NonParticipantUserException;
import com.parkchoi.scrum.domain.team.exception.TeamNotFoundException;
import com.parkchoi.scrum.domain.team.repository.InviteTeamListRepository;
import com.parkchoi.scrum.domain.team.repository.TeamRepository;
import com.parkchoi.scrum.domain.user.entity.QUser;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.exception.UserNotFoundException;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrumService {

    private final UserRepository userRepository;
    private final ScrumRepository scrumRepository;
    private final ScrumParticipantRepository scrumParticipantRepository;
    private final InviteTeamListRepository inviteTeamListRepository;
    private final TeamRepository teamRepository;
    private final JwtUtil jwtUtil;
    private final JPAQueryFactory queryFactory;

    // 스크럼 생성
    @Transactional
    public void createScrum(String accessToken, Long teamId, CreateScrumRequestDTO dto) {
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("DB에서 " + userId + "번 유저를 찾지 못했습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("DB에서 " + teamId + "번 팀을 찾지 못했습니다."));

        try {
            // 스크럼 생성
            Scrum scrum = Scrum.builder()
                    .name(dto.getName())
                    .maxMember(dto.getMaxMember())
                    .currentMember(1)
                    .team(team)
                    .user(user)
                    .subject(dto.getSubject())
                    .build();
            scrumRepository.save(scrum);

            // 스크럼 참여자 정보 생성
            ScrumParticipant scrumParticipant = ScrumParticipant.builder()
                    .scrum(scrum)
                    .user(user).build();
            scrumParticipantRepository.save(scrumParticipant);
        } catch(Exception e) {
            throw new FailCreateScrumException("스크럼 생성에 실패하였습니다.");
        }
    }

    // 스크럼 팀 조회
    public ScrumRoomListResponseDTO findScrums(String accessToken, Long teamId) {
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("DB에서 " + userId + "번 유저를 찾지 못했습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("DB에서 " + teamId + "번 팀을 찾지 못했습니다."));

        inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException(teamId + "번 팀 초대 리스트에" + userId+ "번 유저가 존재 하지 않습니다."));

        List<Scrum> scrumList = scrumRepository.findActiveScrumsByTeam(team);
        List<ScrumRoomDTO> scrumRoomDTOList = new ArrayList<>();

        for (Scrum s : scrumList) {
            if (s.getEndTime() == null) {
                ScrumRoomDTO scrumRoomDTO = ScrumRoomDTO.builder()
                        .scrumId(s.getId())
                        .name(s.getName())
                        .profileImage(s.getUser().getProfileImage())
                        .maxMember(s.getMaxMember())
                        .currentMember(s.getCurrentMember())
                        .isRunning(s.getIsStart())
                        .nickname(s.getUser().getNickname()).build();

                scrumRoomDTOList.add(scrumRoomDTO);
            }
        }

        return new ScrumRoomListResponseDTO(scrumRoomDTOList);
    }


    // 스크럼 참여
    @Transactional
    public void enterScrum(String accessToken, Long teamId, Long scrumId){
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("DB에서 " + userId + "번 유저를 찾지 못했습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("DB에서 " + teamId + "번 팀을 찾지 못했습니다."));

        inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException(teamId + "번 팀 초대 리스트에" + userId+ "번 유저가 존재 하지 않습니다."));

        Scrum scrum = scrumRepository.findActiveScrumByScrumId(scrumId)
                .orElseThrow(() -> new ScrumNotFoundException("DB에서 "+ scrumId+"번 스크럼을 찾지 못했습니다.(삭제 됐거나 종료 됐거나 스크럼이 없음)"));

        // 아직 참여하지 않은 스크럼이면
        if(!scrumParticipantRepository.existsScrumParticipantByUserAndScrum(user, scrum)){
            // 멤버가 꽉찼으면
            if(scrum.getCurrentMember() == scrum.getMaxMember()){
                throw new MaxMemberScrumException("현재 스크럼 인원이 최대입니다. 참여 실패");
            }
            // 참여 생성
            ScrumParticipant scrumParticipant = ScrumParticipant.builder()
                    .scrum(scrum)
                    .user(user).build();
            scrumParticipantRepository.save(scrumParticipant);
            // 현재 인원 증가
            scrum.plusCurrentMember();
        }else{
            throw new AlreadyScrumEnterException(userId + "번 유저는 이미 " + scrumId + "번 스크럼에 참여중 입니다.");
        }
        // 나중에 화면에 필요한 정보 리턴
    }

    // 스크럼 삭제
    @Transactional
    public void removeScrum(String accessToken, Long teamId, Long scrumId) {
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("DB에서 " + userId + "번 유저를 찾지 못했습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("DB에서 " + teamId + "번 팀을 찾지 못했습니다."));

        inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException(teamId + "번 팀 초대 리스트에" + userId+ "번 유저가 존재 하지 않습니다."));

        Scrum scrum = scrumRepository.findActiveScrumByScrumId(scrumId)
                .orElseThrow(() -> new ScrumNotFoundException("DB에서 "+ scrumId+"번 스크럼을 찾지 못했습니다.(삭제 됐거나 종료 됐거나 스크럼이 없음)"));

        if (!scrum.getUser().getId().equals(userId)) {
            throw new NotScrumLeaderException(userId + "번 유저는 " + scrumId + "번 스크럼의 리더가 아니라 삭제 불가능 합니다.");
        }

        if (scrum.getDeleteDate() != null){
            throw new AlreadyScrumRemoveException(scrumId + "번 스크럼은 이미 삭제된 스크럼 입니다.");
        }
        // 스크럼 삭제 진행
        scrum.deleteScrum();
    }

    // 스크럼 시작
    @Transactional
    public void startScrum(String accessToken, Long teamId, Long scrumId){
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("DB에서 " + userId + "번 유저를 찾지 못했습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("DB에서 " + teamId + "번 팀을 찾지 못했습니다."));

        inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException(teamId + "번 팀 초대 리스트에" + userId+ "번 유저가 존재 하지 않습니다."));

        Scrum scrum = scrumRepository.findActiveScrumByScrumId(scrumId)
                .orElseThrow(() -> new ScrumNotFoundException("DB에서 "+ scrumId+"번 스크럼을 찾지 못했습니다.(삭제 됐거나 종료 됐거나 스크럼이 없음)"));

        if (!scrum.getUser().getId().equals(userId)) {
            throw new NotScrumLeaderException(userId + "번 유저는 " + scrumId + "번 스크럼의 리더가 아니라 시작 불가능 합니다.");
        }

        // 이미 스크럼이 시작했다면
        if (scrum.getIsStart()){
            throw new AlreadyScrumStartException(scrumId + "번 스크럼은 이미 시작된 스크럼 입니다.");
        }

        // 시작 상태 변경과 시간 추가
        scrum.startScrum();
    }

    // 스크럼 종료
    @Transactional
    public void endScrum(String accessToken, Long teamId, Long scrumId){
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("DB에서 " + userId + "번 유저를 찾지 못했습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("DB에서 " + teamId + "번 팀을 찾지 못했습니다."));

        inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException(teamId + "번 팀 초대 리스트에" + userId+ "번 유저가 존재 하지 않습니다."));

        Scrum scrum = scrumRepository.findActiveScrumByScrumId(scrumId)
                .orElseThrow(() -> new ScrumNotFoundException("DB에서 "+ scrumId+"번 스크럼을 찾지 못했습니다.(삭제 됐거나 종료 됐거나 스크럼이 없음)"));

        // 이미 삭제된 스크럼이면
        if (scrum.getDeleteDate() != null){
            throw new AlreadyScrumRemoveException(scrumId + "번 스크럼은 이미 삭제된 스크럼 입니다.");
        }

        if (!scrum.getUser().getId().equals(userId)) {
            throw new NotScrumLeaderException(userId + "번 유저는 " + scrumId + "번 스크럼의 리더가 아니라 종료 불가능 합니다.");
        }

        // 아직 스크럼이 시작하지 않았으면
        if (!scrum.getIsStart()){
            throw new NotStartScrumException(scrumId + "번 스크럼은 아직 시작 하지 않았습니다.");
        }

        // 이미 스크럼이 종료 상태라면
        if (scrum.getEndTime() != null){
            throw new AlreadyScrumEndException(scrumId + "번 스크럼은 이미 종료된 스크럼 입니다.");
        }

        scrum.endScrum();
    }

    // 스크럼 생성 가능 여부 확인
    public boolean checkScrumAvailability(String accessToken){
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("DB에서 " + userId + "번 유저를 찾지 못했습니다."));

        boolean result = scrumRepository.existsActiveScrumByUser(user);
        return !result;
    }

    // 페이지네이션 처리
    public ScrumPageResponseDTO searchScrum(String accessToken, String type, String key, Long teamId, Pageable pageable){
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("DB에서 " + userId + "번 유저를 찾지 못했습니다."));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("DB에서 " + teamId + "번 팀을 찾지 못했습니다."));

        inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException(teamId + "번 팀 초대 리스트에" + userId+ "번 유저가 존재 하지 않습니다."));

        Page<Scrum> scrums = scrumRepository.searchScrumWithPagination(type, key, pageable);

        return new ScrumPageResponseDTO(scrums);
    }
}
