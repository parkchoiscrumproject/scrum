package com.parkchoi.scrum.domain.scrum.service;

import com.parkchoi.scrum.domain.scrum.dto.request.CreateScrumRequestDTO;
import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.scrum.entity.ScrumInfo;
import com.parkchoi.scrum.domain.scrum.entity.ScrumParticipant;
import com.parkchoi.scrum.domain.scrum.exception.FailCreateScrumException;
import com.parkchoi.scrum.domain.scrum.repository.ScrumInfoRepository;
import com.parkchoi.scrum.domain.scrum.repository.ScrumParticipantRepository;
import com.parkchoi.scrum.domain.scrum.repository.ScrumRepository;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.team.exception.TeamNotFoundException;
import com.parkchoi.scrum.domain.team.repository.TeamRepository;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.exception.UserNotFoundException;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrumService {

    private final UserRepository userRepository;
    private final ScrumRepository scrumRepository;
    private final ScrumInfoRepository scrumInfoRepository;
    private final ScrumParticipantRepository scrumParticipantRepository;
    private final TeamRepository teamRepository;
    private final JwtUtil jwtUtil;

    // 스크럼 생성
    @Transactional
    public void createScrum(String accessToken, Long teamId, CreateScrumRequestDTO dto){
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        try{
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

        }catch (Exception e){
            throw new FailCreateScrumException("스크럼 생성에 실패하였습니다.");
        }

    }
}
