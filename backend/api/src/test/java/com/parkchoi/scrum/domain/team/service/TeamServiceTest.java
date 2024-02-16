package com.parkchoi.scrum.domain.team.service;

import com.parkchoi.scrum.domain.team.dto.request.CreateTeamRequestDTO;
import com.parkchoi.scrum.domain.team.dto.request.TeamInfoDTO;
import com.parkchoi.scrum.domain.team.dto.response.CreateTeamResponseDTO;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.team.repository.TeamRepository;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import com.parkchoi.scrum.util.s3.S3UploadService;
import org.apache.juli.logging.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TeamService teamService;
    @InjectMocks
    private S3UploadService s3UploadService;

    private String accessToken;
    private Long userId;
    private User mockUser;
    private Long teamId;
    private Team mockTeam;

    @BeforeEach
    void ser_up() throws NoSuchFieldException, IllegalAccessException {
        //given
        accessToken = "test_access_token";
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

    }


    @Test
    void 팀_생성_성공()throws IOException{
        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));


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

        MockMultipartFile file = new MockMultipartFile("file","test.png","image/png","testImageContent".getBytes());


        //when
        CreateTeamResponseDTO result = teamService.createTeam(accessToken, file, createTeamRequestDTO);

        //then
        Mockito.verify(s3UploadService).saveFile(Mockito.any(MultipartFile.class));
        Assertions.assertNotNull(result);
        Assertions.assertEquals("팀이름", result.getName());
        Assertions.assertEquals("image_url", result.getImageUrl());


    }

}
