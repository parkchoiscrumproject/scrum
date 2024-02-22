package com.parkchoi.scrum.domain.team.service;

import com.parkchoi.scrum.domain.team.dto.request.CreateTeamRequestDTO;
import com.parkchoi.scrum.domain.team.dto.request.TeamInfoDTO;
import com.parkchoi.scrum.domain.team.dto.response.CreateTeamResponseDTO;
import com.parkchoi.scrum.domain.team.entity.InviteTeamList;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.team.repository.InviteTeamListRepository;
import com.parkchoi.scrum.domain.team.repository.TeamRepository;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.repository.user.UserRepository;
import com.parkchoi.scrum.util.SecurityContext;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import com.parkchoi.scrum.util.s3.S3UploadService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private S3UploadService s3UploadService;
    @Mock
    private InviteTeamListRepository inviteTeamListRepository;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private TeamService teamService;


    private Long userId;
    private User mockUser;
    private User invitedUser2;
    private User invitedUser3;
    private Long teamId;
    private Team mockTeam;

    @BeforeEach
    void ser_up() throws NoSuchFieldException, IllegalAccessException {
        //given
        userId = 1L;

        //user생성
        mockUser = User.builder()
                .email("test@test.com")
                .profileImage("test")
                .nickname("test")
                .isOnline(true)
                .type("kakao").build();

        //목 유저의 id라는 이름의 필드를 찾고
        Field id = mockUser.getClass().getDeclaredField("id");
        //Java의 접근 제어 메커니즘을 무시하고(private 필드에도 접근 가능)
        id.setAccessible(true);
        //목유저에, 유저 아이디를 담으면 완전한 목 유저 생성
        id.set(mockUser,userId);

        invitedUser2 = User.builder()
                .email("test@test.com")
                .profileImage("test")
                .nickname("test")
                .isOnline(true)
                .type("kakao").build();
        Field id2 = mockUser.getClass().getDeclaredField("id");
        id2.setAccessible(true);
        id2.set(invitedUser2,2L);

        invitedUser3 = User.builder()
                .email("test@test.com")
                .profileImage("test")
                .nickname("test")
                .isOnline(true)
                .type("kakao").build();
        Field id3 = mockUser.getClass().getDeclaredField("id");
        id3.setAccessible(true);
        id3.set(invitedUser3,3L);
    }


    @Test
    void 팀_생성_성공()throws IOException{
        //given
        MockMultipartFile file = new MockMultipartFile("file","test.png","image/png","testImageContent".getBytes(StandardCharsets.UTF_8));

        Mockito.when(securityContext.getUser()).thenReturn(mockUser);

        Mockito.when(s3UploadService.saveFile(file)).thenReturn("image_url");
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(invitedUser2));
        Mockito.when(userRepository.findById(3L)).thenReturn(Optional.of(invitedUser3));


        //팀 정보 생성
        TeamInfoDTO teamInfo = TeamInfoDTO.builder()
                .name("팀이름")
                .description("팀설명")
                .maxMember(3).build();

        //팀
        CreateTeamRequestDTO createTeamRequestDTO = CreateTeamRequestDTO.builder()
                .teamInfoDTO(teamInfo)
                .inviteList(List.of(2L,3L))
                .build();


        //when
        CreateTeamResponseDTO result = teamService.createTeam(file, createTeamRequestDTO);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals("팀이름", result.getName());
        Assertions.assertEquals("image_url", result.getImageUrl());


        //Mockito.verify를 사용하여 모의 객체가 제대로 흘러갔는지 확인
        Mockito.verify(s3UploadService).saveFile(file);
        Mockito.verify(teamRepository).save(Mockito.any(Team.class));
        Mockito.verify(inviteTeamListRepository,Mockito.times(3)).save(Mockito.any(InviteTeamList.class));

    }

}
