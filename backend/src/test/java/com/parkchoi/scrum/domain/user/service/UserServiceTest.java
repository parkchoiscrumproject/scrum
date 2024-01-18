package com.parkchoi.scrum.domain.user.service;

import com.parkchoi.scrum.domain.log.repository.UserLogRepository;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import com.parkchoi.scrum.util.jwt.JwtUtil;
import com.parkchoi.scrum.util.s3.S3UploadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserLogRepository userLogRepository;

    @Mock
    private S3UploadService s3UploadService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @Test
    void logout() {
    }

    @Test
    void 로그인_성공_유저_정보_반환() {
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