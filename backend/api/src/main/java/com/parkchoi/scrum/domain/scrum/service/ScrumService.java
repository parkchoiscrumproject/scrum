package com.parkchoi.scrum.domain.scrum.service;

import com.parkchoi.scrum.domain.scrum.dto.request.CreateScrumRequestDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomListResponseDTO;
import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.scrum.entity.ScrumInfo;
import com.parkchoi.scrum.domain.scrum.entity.ScrumParticipant;
import com.parkchoi.scrum.domain.scrum.exception.*;
import com.parkchoi.scrum.domain.scrum.repository.ScrumInfoRepository;
import com.parkchoi.scrum.domain.scrum.repository.ScrumParticipantRepository;
import com.parkchoi.scrum.domain.scrum.repository.ScrumRepository;
import com.parkchoi.scrum.domain.team.entity.InviteTeamList;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.team.exception.NonParticipantUserException;
import com.parkchoi.scrum.domain.team.exception.TeamNotFoundException;
import com.parkchoi.scrum.domain.team.repository.InviteTeamListRepository;
import com.parkchoi.scrum.domain.team.repository.TeamRepository;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.exception.UserNotFoundException;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrumService {

    private final UserRepository userRepository;
    private final ScrumRepository scrumRepository;
    private final ScrumInfoRepository scrumInfoRepository;
    private final ScrumParticipantRepository scrumParticipantRepository;
    private final InviteTeamListRepository inviteTeamListRepository;
    private final TeamRepository teamRepository;
    private final JwtUtil jwtUtil;

    // 스크럼 생성
    @Transactional
    public void createScrum(String accessToken, Long teamId, CreateScrumRequestDTO dto) {
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        try {
            // 스크럼 생성
            Scrum scrum = Scrum.builder()
                    .name(dto.getName())
                    .maxMember(dto.getMaxMember())
                    .currentMember(1)
                    .team(team)
                    .user(user).build();
            scrumRepository.save(scrum);

            // 스크럼 정보 생성
            ScrumInfo scrumInfo = ScrumInfo.builder()
                    .scrum(scrum)
                    .subject(dto.getSubject())
                    .isStart(false).build();
            scrumInfoRepository.save(scrumInfo);

            // 스크럼 참여자 정보 생성
            ScrumParticipant scrumParticipant = ScrumParticipant.builder()
                    .scrum(scrum)
                    .user(user).build();
            scrumParticipantRepository.save(scrumParticipant);

        } catch (Exception e) {
            throw new FailCreateScrumException("스크럼 생성에 실패하였습니다.");
        }
    }

    // 스크럼 팀 조회
    public ScrumRoomListResponseDTO findScrums(String accessToken, Long teamId) {
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        InviteTeamList inviteTeamList = inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException("해당 유저가 팀에 참여하지 않았습니다."));

        Optional<List<Scrum>> scrumList = scrumRepository.findByTeamWithFetchJoinUserAndScrumInfoAndDeleteDateIsNull(team);

        List<Scrum> scrums = scrumList.orElse(new ArrayList<>());
        List<ScrumRoomDTO> scrumRoomDTOList = new ArrayList<>();

        for(int i=0; i<scrums.size(); i++){
            Scrum s = scrums.get(i);
            if(s.getScrumInfo().getEndTime() == null){
                ScrumRoomDTO scrumRoomDTO = ScrumRoomDTO.builder()
                        .scrumId(s.getId())
                        .name(s.getName())
                        .profileImage(s.getUser().getProfileImage())
                        .maxMember(s.getMaxMember())
                        .currentMember(s.getCurrentMember())
                        .isRunning(s.getScrumInfo().getIsStart())
                        .nickname(s.getUser().getNickname()).build();

                scrumRoomDTOList.add(scrumRoomDTO);
            }
        }

        return new ScrumRoomListResponseDTO(scrumRoomDTOList);
    }

    @Transactional
    // 스크럼 참여
    public void enterScrum(String accessToken, Long teamId, Long scrumId){
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        InviteTeamList inviteTeamList = inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException("해당 유저가 팀에 참여하지 않았습니다."));

        Scrum scrum = scrumRepository.findById(scrumId)
                .orElseThrow(() -> new ScrumNotFoundException("스크럼이 존재하지 않습니다."));

        if(scrum.getDeleteDate() != null){
            throw new NotScrumLeaderException("삭제된 스크럼입니다.");
        }

        ScrumInfo scrumInfo = scrumInfoRepository.findByScrum(scrum);
        // 이미 종료된 스크럼이면
        if(scrumInfo.getEndTime() != null){
            throw new AlreadyScrumEndException("이미 종료된 스크럼입니다.");
        }

        Optional<ScrumParticipant> byScrumAndUser = scrumParticipantRepository.findByUserAndScrum(user, scrum);
        // 아직 참여하지 않은 스크럼이면
        if(byScrumAndUser.isEmpty()){
            // 멤버가 꽉찼으면ㅅ
            if(scrum.getCurrentMember() == scrum.getMaxMember()){
                throw new MaxMemberScrumException("참여 최대 인원에 도달했습니다.");
            }
            // 참여 생성
            ScrumParticipant scrumParticipant = ScrumParticipant.builder()
                    .scrum(scrum)
                    .user(user).build();
            scrumParticipantRepository.save(scrumParticipant);
            // 현재 인원 증가
            scrum.plusCurrentMember();
        }else{
            throw new AlreadyScrumEnterException("이미 스크럼에 참여중입니다.");
        }
        // 나중에 화면에 필요한 정보 리턴
    }

    // 스크럼 삭제
    @Transactional
    public void removeScrum(String accessToken, Long teamId, Long scrumId) {
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        InviteTeamList inviteTeamList = inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException("해당 유저가 팀에 참여하지 않았습니다."));

        Scrum scrum = scrumRepository.findById(scrumId)
                .orElseThrow(() -> new ScrumNotFoundException("스크럼이 존재하지 않습니다."));

        if (!scrum.getUser().getId().equals(userId)) {
            throw new NotScrumLeaderException("리더만 삭제 가능합니다.");
        }

        if (scrum.getDeleteDate() != null){
            throw new AlreadyScrumRemoveException("이미 삭제된 스크럼입니다.");
        }
        // 삭제 시간 추가
        scrum.addDeleteDate();
    }

    // 스크럼 시작
    @Transactional
    public void startScrum(String accessToken, Long teamId, Long scrumId){
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        InviteTeamList inviteTeamList = inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException("해당 유저가 팀에 참여하지 않았습니다."));

        Scrum scrum = scrumRepository.findById(scrumId)
                .orElseThrow(() -> new ScrumNotFoundException("스크럼이 존재하지 않습니다."));

        ScrumInfo scrumInfo = scrumInfoRepository.findByScrum(scrum);

        if (!scrum.getUser().getId().equals(userId)) {
            throw new NotScrumLeaderException("리더만 시작 가능합니다.");
        }

        // 이미 스크럼이 시작했다면
        if (scrumInfo.getIsStart()){
            throw new AlreadyScrumStartException("이미 시작된 스크럼입니다.");
        }

        // 시작 상태 변경과 시간 추가
        scrumInfo.startScrum();
    }

    // 스크럼 종료
    @Transactional
    public void endScrum(String accessToken, Long teamId, Long scrumId){
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        InviteTeamList inviteTeamList = inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException("해당 유저가 팀에 참여하지 않았습니다."));

        Scrum scrum = scrumRepository.findById(scrumId)
                .orElseThrow(() -> new ScrumNotFoundException("스크럼이 존재하지 않습니다."));

        ScrumInfo scrumInfo = scrumInfoRepository.findByScrum(scrum);

        // 이미 삭제된 스크럼이면
        if (scrum.getDeleteDate() != null){
            throw new AlreadyScrumRemoveException("이미 삭제된 스크럼입니다.");
        }

        if (!scrum.getUser().getId().equals(userId)) {
            throw new NotScrumLeaderException("리더만 종료 가능합니다.");
        }

        // 아직 스크럼이 시작하지 않았으면
        if (!scrumInfo.getIsStart()){
            throw new NotStartScrumException("아직 스크럼을 시작하지 않았습니다.");
        }

        // 이미 스크럼이 종료 상태라면
        if (scrumInfo.getEndTime() != null){
            throw new AlreadyScrumEndException("이미 종료된 스크럼입니다.");
        }


        scrumInfo.endScrum();
    }

    // 스크럼 생성 가능 여부 확인
    public boolean checkScrumAvailability(String accessToken){
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        Optional<List<Scrum>> scrumList = scrumRepository.findByUserWithFetchJoinScrumInfoAndDeleteDateIsNullAndEndTimeIsNull(user);
        if(scrumList.isPresent() && !scrumList.get().isEmpty()){
            return false;
        }else{
            return true;
        }
    }

}