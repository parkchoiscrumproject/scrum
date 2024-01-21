package com.parkchoi.scrum.domain.scrum.service;

import com.parkchoi.scrum.domain.scrum.repository.ScrumInfoRepository;
import com.parkchoi.scrum.domain.scrum.repository.ScrumParticipantRepository;
import com.parkchoi.scrum.domain.scrum.repository.ScrumRepository;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.team.repository.InviteTeamListRepository;
import com.parkchoi.scrum.domain.team.repository.TeamRepository;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ScrumServiceTest {

    @Mock
    private ScrumRepository scrumRepository;

    @Mock
    private ScrumInfoRepository scrumInfoRepository;

    @Mock
    private ScrumParticipantRepository scrumParticipantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InviteTeamListRepository inviteTeamListRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private ScrumService scrumService;


    @Test
    void 스크럼_생성_성공() {
        // given
        String accessToken = "test_access_token";
        Long userId = 1L;
        User mockUser = User.builder()
                .email("test@test.com")
                .profileImage("test")
                .nickname("test")
                .isOnline(true)
                .type("kakao").build();

        Long teamId = 1L;
        Team mockTeam = Team.builder()
                .name("팀이름")
                .teamProfileImage("팀사진")
                .description("팀설명")
                .currentMember(1)
                .maxMember(15)
                .user(mockUser)
                .build();

        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(teamRepository.findById(teamId)).thenReturn(Optional.of(mockTeam));
    }

    @Test
    void findScrums() {
    }

    @Test
    void enterScrum() {
    }

    @Test
    void removeScrum() {
    }

    @Test
    void startScrum() {
    }

    @Test
    void endScrum() {
    }
}