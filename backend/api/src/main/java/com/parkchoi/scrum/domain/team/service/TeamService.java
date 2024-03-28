package com.parkchoi.scrum.domain.team.service;

import com.parkchoi.scrum.domain.exception.exception.UserException;
import com.parkchoi.scrum.domain.exception.info.UserExceptionInfo;
import com.parkchoi.scrum.domain.team.dto.request.CreateTeamRequestDTO;
import com.parkchoi.scrum.domain.team.dto.request.KickTeamRequestDTO;
import com.parkchoi.scrum.domain.team.dto.request.TeamInvitationRequestDTO;
import com.parkchoi.scrum.domain.team.dto.response.*;
import com.parkchoi.scrum.domain.team.entity.InviteTeamList;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.team.exception.*;
import com.parkchoi.scrum.domain.team.repository.InviteTeamListRepository;
import com.parkchoi.scrum.domain.team.repository.TeamRepository;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.exception.UserNotFoundException;
import com.parkchoi.scrum.domain.user.repository.user.UserRepository;
import com.parkchoi.scrum.util.SecurityContext;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import com.parkchoi.scrum.util.s3.S3UploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final InviteTeamListRepository inviteTeamListRepository;
    private final S3UploadService s3UploadService;
    private final SecurityContext securityContext;

    //팀 생성
    @Transactional
    public CreateTeamResponseDTO createTeam(MultipartFile file, CreateTeamRequestDTO dto) {
        User user = securityContext.getUser();

        String imageUrl = null;

        try {
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

            if (inviteList != null && !inviteList.isEmpty()) {
                for (Long inviteUserId : inviteList) {

                    User inviteeUser = userRepository.findById(inviteUserId).get();

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

            return new CreateTeamResponseDTO(team.getName(), imageUrl);

        } catch (Exception e) {
            if (imageUrl != null) {
                s3UploadService.deleteFile(imageUrl);
            }
            throw new FailCreateTeamException("팀 생성에 실패하였습니다.");
        }
    }


    // 팀 삭제
    @Transactional
    public void removeTeam(Long teamId) {
        User user = securityContext.getUser();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        if (!team.getUser().equals(user)) {
            throw new NoTeamLeaderException("리더만 삭제 가능합니다.");
        }

        //팀 삭제
        teamRepository.deleteById(teamId);
    }


    // 팀원 초대
    @Transactional
    public void inviteTeamMember(Long teamId, TeamInvitationRequestDTO dto) {
        User user = securityContext.getUser();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        List<Long> inviteeUserIds = dto.getInviteList();

        for (Long inviteeUserId : inviteeUserIds) {
            User invitee = userRepository.findById(inviteeUserId)
                    .orElseThrow(() -> new UserException(UserExceptionInfo.NOT_FOUNT_USER, "DB에서 " + "번 유저를 찾지 못했습니다."));

            InviteTeamList invite = InviteTeamList.builder()
                    .user(invitee)
                    .team(team)
                    .participant(false)
                    .build();

            inviteTeamListRepository.save(invite);
        }
    }

    // 팀 초대 승낙
    @Transactional
    public void acceptTeamInvite(Long teamId, Long inviteId) {
        User user = securityContext.getUser();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        InviteTeamList teamList = inviteTeamListRepository.findById(inviteId)
                .orElseThrow(() -> new InviteNotFoundException("초대가 존재하지 않습니다."));

        //초대 승낙
        teamList.acceptInvitation();
    }


    // 팀 초대 거절
    @Transactional
    public void refuseTeamInvite(Long teamId, Long inviteId) {
        User user = securityContext.getUser();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        inviteTeamListRepository.deleteById(inviteId);
    }


    // 나의 팀 목록 조회
    public TeamListResponseDTO findMyTeams() {
        User user = securityContext.getUser();

        List<InviteTeamList> inviteTeamList = inviteTeamListRepository.findByUserAndParticipantIsTrue(user)
                .orElse(new ArrayList<>());

        if (inviteTeamList.isEmpty()) {
            throw new NonParticipantUserException("해당 유저가 팀에 참여하지 않았습니다.");
        }

        List<TeamDTO> teamDTOs = inviteTeamList.stream()
                .map(inviteTeam -> {
                    Team team = inviteTeam.getTeam();
                    return TeamDTO.builder()
                            .teamId(team.getId())
                            .name(team.getName())
                            .currentMember(team.getCurrentMember()) // 이 메서드는 팀의 현재 멤버 수를 반환해야 합니다.
                            .teamProfileImage(team.getTeamProfileImage()) // 이 메서드는 팀 프로필 이미지의 URL을 반환해야 합니다.
                            .build();
                })
                .collect(Collectors.toList());


        return new TeamListResponseDTO(teamDTOs);
    }


    // 팀에 속해있는 멤버들을 조회
    public MemberListResponseDTO findTeamMembers(Long teamId) {
        User user = securityContext.getUser();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        List<InviteTeamList> inviteTeamLists = inviteTeamListRepository.findByTeamAndParticipantIsTrue(team)
                .orElse(new ArrayList<>());

        if (inviteTeamLists.isEmpty()) {
            throw new NonParticipantUserException("해당 유저가 팀에 참여하지 않았습니다.");
        }

        List<MemberDTO> members = inviteTeamLists.stream()
                .map(member -> {
                    User u = member.getUser();
                    return MemberDTO.builder()
                            .userId(u.getId())
                            .nickname(u.getNickname())
                            .statusMessage(u.getStatusMessage())
                            .profileImage(u.getProfileImage())
                            .isOnline(u.getIsOnline())
                            .build();
                })
                .collect(Collectors.toList());

        return new MemberListResponseDTO(members);
    }

    // 팀 나가기
    @Transactional
    public void leaveTeam(Long teamId) {
        User user = securityContext.getUser();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        InviteTeamList inviteTeamList = inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(user, team)
                .orElseThrow(() -> new NonParticipantUserException("해당 유저가 팀에 참여하지 않았습니다."));

        //멤버 감소
        team.decreaseCurrentMember();

        inviteTeamListRepository.delete(inviteTeamList);

    }

    // 팀원 추방
    @Transactional
    public void kickTeam(Long teamId, KickTeamRequestDTO dto) {
        User user = securityContext.getUser();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("팀이 존재하지 않습니다."));

        if (team.getUser() != user) {
            throw new NoTeamLeaderException("리더만 삭제 가능합니다.");
        } else {
            Long kickUserId = dto.getUserId();
            User kickUser = userRepository.findById(kickUserId)
                    .orElseThrow(() -> new UserException(UserExceptionInfo.NOT_FOUNT_USER, "DB에서 " + "번 유저를 찾지 못했습니다."));

            //멤버 감소
            team.decreaseCurrentMember();

            inviteTeamListRepository.deleteByUserAndTeamAndParticipantIsTrue(kickUser, team);
        }
    }

}
