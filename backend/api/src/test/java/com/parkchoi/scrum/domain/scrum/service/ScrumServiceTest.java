package com.parkchoi.scrum.domain.scrum.service;

import com.parkchoi.scrum.domain.scrum.dto.request.CreateScrumRequestDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomDTO;
import com.parkchoi.scrum.domain.scrum.dto.response.ScrumRoomListResponseDTO;
import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.scrum.entity.ScrumParticipant;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ScrumServiceTest {

    @Mock
    private ScrumRepository scrumRepository;


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

    private String accessToken;
    private Long userId;
    private User mockUser;
    private Long teamId;
    private Team mockTeam;

    @BeforeEach
    void set_up() throws NoSuchFieldException, IllegalAccessException {
        // given
        accessToken = "test_access_token";
        userId = 1L;
        mockUser = User.builder()
                .email("test@test.com")
                .profileImage("test")
                .nickname("test")
                .isOnline(true)
                .type("kakao").build();

        Field id = mockUser.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(mockUser, userId);

        teamId = 1L;
        mockTeam = Team.builder()
                .name("팀이름")
                .teamProfileImage("팀사진")
                .description("팀설명")
                .currentMember(1)
                .maxMember(15)
                .user(mockUser)
                .build();
    }


    @Test
    void 스크럼_생성_성공() {
        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(teamRepository.findById(teamId)).thenReturn(Optional.of(mockTeam));

        CreateScrumRequestDTO createScrumRequestDTO = CreateScrumRequestDTO.builder()
                .maxMember(15)
                .name("이름")
                .subject("주제").build();

        // when
        scrumService.createScrum(accessToken, teamId, createScrumRequestDTO);

        // then
        // 스크럼 클래스를 잡기 위한 초기 설정
        ArgumentCaptor<Scrum> scrumArgumentCaptor = ArgumentCaptor.forClass(Scrum.class);
        // save에 들어오는 객체를 캡쳐함.
        Mockito.verify(scrumRepository).save(scrumArgumentCaptor.capture());
        Assertions.assertEquals("이름", scrumArgumentCaptor.getValue().getName());
        Assertions.assertEquals(15, scrumArgumentCaptor.getValue().getMaxMember());
        Assertions.assertEquals("주제", scrumArgumentCaptor.getValue().getSubject());

        ArgumentCaptor<ScrumParticipant> scrumParticipantArgumentCaptor = ArgumentCaptor.forClass(ScrumParticipant.class);
        Mockito.verify(scrumParticipantRepository).save(scrumParticipantArgumentCaptor.capture());
        Assertions.assertEquals(mockUser, scrumParticipantArgumentCaptor.getValue().getUser());
        Assertions.assertEquals(scrumArgumentCaptor.getValue(), scrumParticipantArgumentCaptor.getValue().getScrum());
    }

    @Test
    void 스크럼_목록_조회_성공() {
        // given
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
                .team(mockTeam)
                .subject("주제").build();
        mockScrumList.add(mockScrum);

        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(teamRepository.findById(teamId)).thenReturn(Optional.of(mockTeam));
        Mockito.when(inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(mockUser, mockTeam)).thenReturn(Optional.of(mockInviteTeamList));
        Mockito.when(scrumRepository.findByTeamWithFetchJoinUserAndDeleteDateIsNull(mockTeam)).thenReturn(mockScrumList);

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
    void 스크럼_입장_성공() {
        // given
        InviteTeamList mockInviteTeamList = InviteTeamList.builder()
                .user(mockUser)
                .team(mockTeam)
                .participant(true).build( );

        Long scrumId = 1L;
        Scrum mockScrum = Scrum.builder()
                .team(mockTeam)
                .user(mockUser)
                .currentMember(1)
                .name("스크럼")
                .maxMember(15)
                .subject("주제").build();

        ScrumParticipant mockNullScrumParticipant = null;

        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(teamRepository.findById(teamId)).thenReturn(Optional.of(mockTeam));
        Mockito.when(inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(mockUser, mockTeam)).thenReturn(Optional.of(mockInviteTeamList));
        Mockito.when(scrumRepository.findById(scrumId)).thenReturn(Optional.of(mockScrum));
//        Mockito.when(scrumParticipantRepository.findByUserAndScrum(mockUser, mockScrum)).thenReturn(Optional.ofNullable(mockNullScrumParticipant));

        // when
        scrumService.enterScrum(accessToken, teamId, scrumId);

        // then
        ArgumentCaptor<ScrumParticipant> scrumParticipantArgumentCaptor = ArgumentCaptor.forClass(ScrumParticipant.class);
        Mockito.verify(scrumParticipantRepository).save(scrumParticipantArgumentCaptor.capture());

        Assertions.assertNull(mockScrum.getEndTime());
        Assertions.assertNotEquals(mockScrum.getMaxMember(), mockScrum.getCurrentMember());
        Assertions.assertEquals(mockUser, scrumParticipantArgumentCaptor.getValue().getUser());
        Assertions.assertEquals(mockScrum, scrumParticipantArgumentCaptor.getValue().getScrum());
        Assertions.assertNotEquals(mockScrum.getCurrentMember() + 1, scrumParticipantArgumentCaptor.getValue().getScrum().getCurrentMember());
    }

    @Test
    void 스크럼_삭제_성공() {
        // given
        InviteTeamList mockInviteTeamList = InviteTeamList.builder()
                .user(mockUser)
                .team(mockTeam)
                .participant(true).build( );

        Long scrumId = 1L;
        Scrum mockScrum = Scrum.builder()
                .team(mockTeam)
                .user(mockUser)
                .currentMember(1)
                .name("스크럼")
                .maxMember(15).build();

        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(teamRepository.findById(teamId)).thenReturn(Optional.of(mockTeam));
        Mockito.when(inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(mockUser, mockTeam)).thenReturn(Optional.of(mockInviteTeamList));
        Mockito.when(scrumRepository.findById(scrumId)).thenReturn(Optional.of(mockScrum));

        // when
        scrumService.removeScrum(accessToken, teamId, scrumId);

        // then
        Assertions.assertEquals(userId, mockScrum.getUser().getId());
        Assertions.assertNotNull(mockScrum.getDeleteDate());
    }

    @Test
    void 스크럼_시작_성공() {
        // given
        InviteTeamList mockInviteTeamList = InviteTeamList.builder()
                .user(mockUser)
                .team(mockTeam)
                .participant(true).build( );

        Long scrumId = 1L;
        Scrum mockScrum = Scrum.builder()
                .team(mockTeam)
                .user(mockUser)
                .currentMember(1)
                .name("스크럼")
                .subject("주제")
                .maxMember(15).build();

        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(teamRepository.findById(teamId)).thenReturn(Optional.of(mockTeam));
        Mockito.when(inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(mockUser, mockTeam)).thenReturn(Optional.of(mockInviteTeamList));
        Mockito.when(scrumRepository.findById(scrumId)).thenReturn(Optional.of(mockScrum));

        // when
        scrumService.startScrum(accessToken, teamId, scrumId);

        // then
        Assertions.assertEquals(userId, mockScrum.getUser().getId());
        Assertions.assertTrue(mockScrum.getIsStart());
        Assertions.assertNotNull(mockScrum.getStartTime());
    }

    @Test
    void 스크럼_종료_성공() throws NoSuchFieldException, IllegalAccessException {
        // given
        InviteTeamList mockInviteTeamList = InviteTeamList.builder()
                .user(mockUser)
                .team(mockTeam)
                .participant(true).build( );

        Long scrumId = 1L;
        Scrum mockScrum = Scrum.builder()
                .team(mockTeam)
                .user(mockUser)
                .currentMember(1)
                .name("스크럼")
                .subject("주제")
                .maxMember(15).build();
        Field isStart = mockScrum.getClass().getDeclaredField("isStart");
        isStart.setAccessible(true);
        isStart.set(mockScrum, true);


        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(teamRepository.findById(teamId)).thenReturn(Optional.of(mockTeam));
        Mockito.when(inviteTeamListRepository.findByUserAndTeamAndParticipantIsTrue(mockUser, mockTeam)).thenReturn(Optional.of(mockInviteTeamList));
        Mockito.when(scrumRepository.findById(scrumId)).thenReturn(Optional.of(mockScrum));

        // when
        scrumService.endScrum(accessToken, teamId, scrumId);

        // then
        Assertions.assertNull(mockScrum.getDeleteDate());
        Assertions.assertEquals(userId, mockScrum.getUser().getId());
        Assertions.assertTrue(mockScrum.getIsStart());
        Assertions.assertNotNull(mockScrum.getEndTime());
    }
}