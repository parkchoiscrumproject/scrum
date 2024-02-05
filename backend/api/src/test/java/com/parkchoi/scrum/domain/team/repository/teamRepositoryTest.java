package com.parkchoi.scrum.domain.team.repository;

import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class teamRepositoryTest {


    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private Team team;

    @BeforeEach
    void setUp(){

        //유저 객체 생성
        user = User.builder()
                .email("test@test.com")
                .profileImage("ex")
                .nickname("ex")
                .isOnline(false)
                .type("kakao").build();
        userRepository.save(user);

        //팀 객체 생성
        team = Team.builder()
                .user(user)
                .maxMember(15)
                .name("팀이름")
                .currentMember(1)
                .description("팀설명")
                .teamProfileImage("팀프로필사진")
                .build();
        teamRepository.save(team);

    }


}
