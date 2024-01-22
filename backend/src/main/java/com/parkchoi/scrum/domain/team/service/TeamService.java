package com.parkchoi.scrum.domain.team.service;

import com.parkchoi.scrum.domain.team.dto.request.CreateTeamRequestDTO;
import com.parkchoi.scrum.domain.team.dto.response.CreateTeamResponseDTO;
import com.parkchoi.scrum.domain.team.entity.InviteTeamList;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.team.exception.FailCreateTeamException;
import com.parkchoi.scrum.domain.team.exception.NoTeamLeaderException;
import com.parkchoi.scrum.domain.team.exception.TeamNotFoundException;
import com.parkchoi.scrum.domain.team.repository.InviteTeamListRepository;
import com.parkchoi.scrum.domain.team.repository.TeamRepository;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.exception.UserNotFoundException;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import com.parkchoi.scrum.util.s3.S3UploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final InviteTeamListRepository inviteTeamListRepository;
    private final S3UploadService s3UploadService;
    private final JwtUtil jwtUtil;

    //팀 생성
    @Transactional
    public CreateTeamResponseDTO createTeam(String accessToken, MultipartFile file, CreateTeamRequestDTO dto) {
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        String imageUrl = null;

        try{
            //파일 저장
            imageUrl = s3UploadService.saveFile(file);

            //팀 생성
            Team team = Team.builder()
                    .name(dto.getTeamInfoDTO().getName())
                    .description(dto.getTeamInfoDTO().getDescription())
                    .currentMember(1)
                    .maxMember(dto.getTeamInfoDTO().getMaxMember())
                    .user(user)
                    .teamProfileImage(imageUrl).build();

            teamRepository.save(team);

            //팀 초대
            List<Long> inviteList = dto.getInviteList();

            if(inviteList!=null && !inviteList.isEmpty()){
                for(Long inviteUserId : inviteList){

                    User inviteeUser  = userRepository.findById(inviteUserId).get();

                    InviteTeamList inviteTeamList = InviteTeamList.builder()
                            .user(inviteeUser)
                            .team(team)
                            .participant(false)
                            .build();
                    inviteTeamListRepository.save(inviteTeamList);
                }
            }

            // 현재 유저도 팀 초대 목록에 생성(참여)
            InviteTeamList curUser = InviteTeamList.builder()
                    .user(user)
                    .team(team)
                    .participant(true).build();
            inviteTeamListRepository.save(curUser);

            return new CreateTeamResponseDTO(team.getName(),imageUrl);

        } catch (Exception e) {
            if(imageUrl != null){
                s3UploadService.deleteFile(imageUrl);
            }
            throw new FailCreateTeamException("팀 생성에 실패하였습니다.");
        }
    }


    // 팀 삭제
    @Transactional
    public void removeTeam(String accessToken, Long teamId){
        Long userId = jwtUtil.getUserId(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 없음"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        if(!team.getUser().getId().equals(userId)){
            throw new NoTeamLeaderException("리더만 삭제 가능합니다.");
        }
        
        //팀 삭제
        teamRepository.deleteByIdAndUserId(teamId,userId);
    }
}
