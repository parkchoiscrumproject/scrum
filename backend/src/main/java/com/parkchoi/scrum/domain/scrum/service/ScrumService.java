package com.parkchoi.scrum.domain.scrum.service;

import com.parkchoi.scrum.domain.scrum.dto.request.CreateScrumRequestDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomListResponseDTO;
import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.scrum.entity.ScrumInfo;
import com.parkchoi.scrum.domain.scrum.entity.ScrumParticipant;
import com.parkchoi.scrum.domain.scrum.exception.FailCreateScrumException;
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

        Optional<List<Scrum>> byTeam = scrumRepository.findByTeamWithUserFetchJoin(team);
        List<Scrum> scrums = byTeam.orElse(new ArrayList<>());

        List<ScrumRoomDTO> scrumRoomDTOList = new ArrayList<>();

        for(Scrum s : scrums){
            ScrumRoomDTO scrumRoomDTO = ScrumRoomDTO.builder()
                    .name(s.getName())
                    .profileImage(s.getUser().getProfileImage())
                    .maxMember(s.getMaxMember())
                    .currentMember(s.getCurrentMember())
                    .nickname(s.getUser().getNickname()).build();

            scrumRoomDTOList.add(scrumRoomDTO);
        }

        return new ScrumRoomListResponseDTO(scrumRoomDTOList);
    }

}
