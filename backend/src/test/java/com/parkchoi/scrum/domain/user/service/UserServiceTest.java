package com.parkchoi.scrum.domain.user.service;

import com.parkchoi.scrum.domain.log.repository.UserLogRepository;
import com.parkchoi.scrum.domain.user.dto.response.UserLoginInfoResponseDTO;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import com.parkchoi.scrum.util.s3.S3UploadService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserLogRepository userLogRepository;

    @Mock
    private HttpServletResponse response;

    @Mock
    private S3UploadService s3UploadService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {

    }


    @Test
    void 로그아웃_성공() {
        // given
        String accessToken = "test_access_token";
        Long userId = 1L;
        User mockUser = User.builder()
                .email("test@test.com")
                .profileImage("test")
                .nickname("test")
                .type("kakao").build();

        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        
        // when
        userService.logout(accessToken, response);

        // then
        Assertions.assertEquals(mockUser.getIsOnline(), false);
        
    }

    @Test
    void 로그인_성공_유저_정보_반환_성공() {
        // given
        String accessToken = "test_access_token";
        Long userId = 1L;
        User mockUser = User.builder()
                .email("test@test.com")
                .profileImage("test")
                .nickname("test")
                .type("kakao").build();

        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // when
        UserLoginInfoResponseDTO result = userService.getUserInfo(accessToken);

        // then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getEmail(), mockUser.getEmail());
        Assertions.assertEquals(result.getNickname(), mockUser.getNickname());
        Assertions.assertEquals(result.getStatusMessage(), mockUser.getStatusMessage());
        Assertions.assertEquals(result.getProfileImage(), mockUser.getProfileImage());
        Assertions.assertEquals(result.getIsOnline(), true);
    }

    @Test
    void checkDuplicationNickname() {
    }

    @Test
    void findUserInfoToEmail() {
    }

    @Test
    void updateUserNickname() {
    }

    @Test
    void updateUserProfileImage() {
    }
}