package com.parkchoi.scrum.domain.user.repository;

import com.parkchoi.scrum.domain.user.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        User user = User.builder()
                .email("test@test.com")
                .profileImage("ex")
                .nickname("ex")
                .isOnline(false)
                .type("kakao").build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("이메일로 회원 검색")
    void 이메일로_회원_찾기(){
        // given
        String email = "test@test.com";
        // when
        Optional<User> byEmail = userRepository.findByEmail(email);

        // then
        Assertions.assertTrue(byEmail.isPresent());
        Assertions.assertEquals(byEmail.get().getEmail(), email);
    }

    @Test
    @DisplayName("닉네임 존재 여부 테스트(존재함)")
    void 중복된_닉네임_존재(){
        // given
        String nickname = "ex";

        // when
        Boolean result = userRepository.existsByNickname(nickname);

        // then
        Assertions.assertEquals(result, true);
    }

    @Test
    @DisplayName("닉네임 존재 여부 테스트(존재하지 않음)")
    void 중복된_닉네임_존재하지_않음(){
        // given
        String nickname = "sad";

        // when
        Boolean result = userRepository.existsByNickname(nickname);

        // then
        Assertions.assertEquals(result, false);
    }

}
