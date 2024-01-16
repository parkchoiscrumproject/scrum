package com.parkchoi.scrum.domain.team.service;

import com.parkchoi.scrum.domain.team.dto.request.CreateTeamRequestDTO;
import com.parkchoi.scrum.domain.team.entity.InviteTeamList;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.team.exception.FailCreateTeamException;
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

import java.io.IOException;
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
    public void createTeam(String accessToken, MultipartFile file, CreateTeamRequestDTO dto) throws IOException {
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
            for(Long inviteUserId : inviteList){
                User inviteeUser  = userRepository.findById(inviteUserId).get();

                InviteTeamList inviteTeamList = InviteTeamList.builder()
                        .user(inviteeUser)
                        .team(team)
                        .build();
                inviteTeamListRepository.save(inviteTeamList);
            }

        }catch (Exception e) {
            if(imageUrl != null){
                s3UploadService.deleteFile(imageUrl);
            }
            throw new FailCreateTeamException("팀 생성에 실패하였습니다.");
        }
    }

}
