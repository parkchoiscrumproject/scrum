package com.parkchoi.scrum.domain.scrum.service;

import com.parkchoi.scrum.domain.scrum.dto.request.CreateScrumRequestDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomListResponseDTO;
import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.scrum.entity.ScrumInfo;
import com.parkchoi.scrum.domain.scrum.entity.ScrumParticipant;
import com.parkchoi.scrum.domain.scrum.repository.ScrumInfoRepository;
import com.parkchoi.scrum.domain.scrum.repository.ScrumParticipantRepository;
import com.parkchoi.scrum.domain.scrum.repository.ScrumRepository;
import com.parkchoi.scrum.domain.team.entity.InviteTeamList;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.team.repository.InviteTeamListRepository;
import com.parkchoi.scrum.domain.team.repository.TeamRepository;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
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

        CreateScrumRequestDTO createScrumRequestDTO = new CreateScrumRequestDTO("이름", 15, "주제");

        // when
        scrumService.createScrum(accessToken, teamId, createScrumRequestDTO);

        // then
        // 스크럼 클래스를 잡기 위한 초기 설정
        ArgumentCaptor<Scrum> scrumArgumentCaptor = ArgumentCaptor.forClass(Scrum.class);
        // save에 들어오는 객체를 캡쳐함.
        Mockito.verify(scrumRepository).save(scrumArgumentCaptor.capture());
        Assertions.assertEquals("이름", scrumArgumentCaptor.getValue().getName());
        Assertions.assertEquals(15, scrumArgumentCaptor.getValue().getMaxMember());

        ArgumentCaptor<ScrumInfo> scrumInfoArgumentCaptor = ArgumentCaptor.forClass(ScrumInfo.class);
        Mockito.verify(scrumInfoRepository).save(scrumInfoArgumentCaptor.capture());
        Assertions.assertEquals("주제", scrumInfoArgumentCaptor.getValue().getSubject());

        ArgumentCaptor<ScrumParticipant> scrumParticipantArgumentCaptor = ArgumentCaptor.forClass(ScrumParticipant.class);
        Mockito.verify(scrumParticipantRepository).save(scrumParticipantArgumentCaptor.capture());
        Assertions.assertEquals(mockUser, scrumParticipantArgumentCaptor.getValue().getUser());
        Assertions.assertEquals(scrumArgumentCaptor.getValue(), scrumParticipantArgumentCaptor.getValue().getScrum());
    }

    @Test
    void 스크럼_목록_조회_성공() {
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

        InviteTeamList mockInviteTeamList = InviteTeamList.builder()
                .user(mockUser)
                .team(mockTeam)
                .participant(true).build( );

        List<Scrum> mockScrumList = new ArrayList<>();
        Scrum mockScrum = Scrum.builder()
                .name("스크럼 이름")
                .maxMember(10)
                .currentMember(1)
                .user(mockUser)
                .team(mockTeam).build();
        mockScrum.addScrumInfo(
                ScrumInfo.builder()
                .scrum(mockScrum)
                .subject("주제")
                .isStart(false).build());
        mockScrumList.add(mockScrum);

        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(teamRepository.findById(teamId)).thenReturn(Optional.of(mockTeam));
        Mockito.when(inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(mockUser, mockTeam)).thenReturn(Optional.of(mockInviteTeamList));
        Mockito.when(scrumRepository.findByTeamWithFetchJoinUserAndScrumInfoAndDeleteDateIsNull(mockTeam)).thenReturn(Optional.of(mockScrumList));

        // when
        ScrumRoomListResponseDTO scrums = scrumService.findScrums(accessToken, teamId);

        // then
        Assertions.assertFalse(scrums.getScrums().isEmpty());
        ScrumRoomDTO scrumRoomDTO = scrums.getScrums().get(0);
        Assertions.assertEquals(mockScrum.getId(), scrumRoomDTO.getScrumId());
        Assertions.assertEquals(mockScrum.getUser().getNickname(), scrumRoomDTO.getNickname());
        Assertions.assertEquals(mockScrum.getMaxMember(), scrumRoomDTO.getMaxMember());
        Assertions.assertEquals(mockScrum.getUser().getProfileImage(), scrumRoomDTO.getProfileImage());
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