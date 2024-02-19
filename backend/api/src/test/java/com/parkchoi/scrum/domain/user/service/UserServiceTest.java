package com.parkchoi.scrum.domain.user.service;

import com.parkchoi.scrum.domain.log.repository.UserLogRepository;
import com.parkchoi.scrum.domain.user.dto.response.UserInviteInfoResponseDTO;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.repository.user.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import com.parkchoi.scrum.util.s3.S3UploadService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    @Test
    void 로그아웃_성공() {
        // given
        String accessToken = "test_access_token";
        Long userId = 1L;
        User mockUser = User.builder()
                .email("test@test.com")
                .profileImage("test")
                .nickname("test")
                .isOnline(true)
                .type("kakao").build();

        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        
        // when
        userService.logout(accessToken, response);

        // then
        Assertions.assertEquals(false, mockUser.getIsOnline());
        Mockito.verify(response).addCookie(Mockito.argThat(cookie -> "accessToken".equals(cookie.getName()) && cookie.getMaxAge() == 0));
        Mockito.verify(response).addCookie(Mockito.argThat(cookie -> "refreshToken".equals(cookie.getName()) && cookie.getMaxAge() == 0));
        Mockito.verify(response).addCookie(Mockito.argThat(cookie -> "JSESSIONID".equals(cookie.getName()) && cookie.getMaxAge() == 0));
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
//        UserLoginInfoResponseDTO result = userService.getUserInfo(accessToken);

        // then
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(result.getEmail(), mockUser.getEmail());
//        Assertions.assertEquals(result.getNickname(), mockUser.getNickname());
//        Assertions.assertEquals(result.getStatusMessage(), mockUser.getStatusMessage());
//        Assertions.assertEquals(result.getProfileImage(), mockUser.getProfileImage());
//        Assertions.assertEquals(result.getIsOnline(), true);
    }

    @Test
    void 닉네임_중복_검사_중복되지_않음() {
        // given
        String nickname = "qwer";
        Mockito.when(userRepository.existsByNickname(nickname)).thenReturn(false);

        // when
        boolean result = userService.checkDuplicationNickname(nickname);

        // then
        Assertions.assertFalse(result);
    }

    @Test
    void 닉네임_중복_검사_중복됨() {
        // given
        String nickname = "test";
        Mockito.when(userRepository.existsByNickname(nickname)).thenReturn(true);

        // when
        boolean result = userService.checkDuplicationNickname(nickname);

        // then
        Assertions.assertTrue(result);
    }

    @Test
    void 이메일로_유저_검색_null_반환() {
        // given
        String email = "test@test.com";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        UserInviteInfoResponseDTO userInfoToEmail = userService.findUserInfoToEmail(email);

        // then
        Assertions.assertNull(userInfoToEmail);
    }

    @Test
    void 이메일로_유저_검색_유저_있음() {
        // given
        String email = "test";
        User mockUser = User.builder()
                .email("test")
                .profileImage("test")
                .nickname("test")
                .type("kakao").build();

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // when
        UserInviteInfoResponseDTO userInfoToEmail = userService.findUserInfoToEmail(email);

        // then
        // dto가 널이 아니고, 유저 정보가 같은지 확인
        Assertions.assertNotNull(userInfoToEmail);
        Assertions.assertEquals(mockUser.getProfileImage(), userInfoToEmail.getProfileImage());
        Assertions.assertEquals(mockUser.getNickname(), userInfoToEmail.getNickname());
        Assertions.assertEquals(mockUser.getId(), userInfoToEmail.getUserId());
    }

    @Test
    void 유저_닉네임_변경_성공() {
        // given
        String accessToken = "test_access_token";
        Long userId = 1L;
        String nickname = "qwer";
        User mockUser = User.builder()
                .email("test@test.com")
                .profileImage("test")
                .nickname("test")
                .type("kakao").build();

        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // when
        userService.updateUserNickname(accessToken, nickname);

        // then
        Assertions.assertEquals(nickname, mockUser.getNickname());
    }

    @Test
    void 유저_프로필사진_변경_성공() throws IOException {
        // given
        String accessToken = "test_access_token";
        Long userId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
        User mockUser = User.builder()
                .email("test@test.com")
                .profileImage("test")
                .nickname("test")
                .type("kakao").build();

        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(s3UploadService.saveFile(file)).thenReturn("qwer");

        // when
        userService.updateUserProfileImage(accessToken, file);

        // then
        Assertions.assertNotEquals("test", mockUser.getProfileImage());
        Assertions.assertEquals("qwer", mockUser.getProfileImage());
    }

    @Test
    void 유저_프로필사진_변경_실패_입출력예외() throws IOException {
        // given
        String accessToken = "test_access_token";
        Long userId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
        User mockUser = User.builder()
                .email("test@test.com")
                .profileImage("test")
                .nickname("test")
                .type("kakao").build();

        Mockito.when(jwtUtil.getUserId(accessToken)).thenReturn(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(s3UploadService.saveFile(file)).thenThrow(IOException.class);
        
        // when && then
        Assertions.assertThrows(IOException.class,()->{
            userService.updateUserProfileImage(accessToken, file);
        });
    }

    @Test
    void 상태메시지_변경_성공(){
        // given
        String statusMessage = "이것은 변경하려는 상태메시지 입니다.";
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
        userService.updateUserStatusMessage(accessToken, statusMessage);

        // then
        Assertions.assertEquals(statusMessage, mockUser.getStatusMessage());
        Assertions.assertNotEquals(null, mockUser.getStatusMessage());

    }
}