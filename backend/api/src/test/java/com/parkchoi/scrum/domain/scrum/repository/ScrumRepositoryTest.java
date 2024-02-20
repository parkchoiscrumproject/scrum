package com.parkchoi.scrum.domain.scrum.repository;

import com.parkchoi.scrum.domain.config.TestConfig;
import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.scrum.repository.scrum.ScrumRepository;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.team.repository.TeamRepository;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ScrumRepositoryTest {

    @Autowired
    private ScrumRepository scrumRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TestEntityManager tem;

    private Team team;
    private User user;
    private Scrum scrum;
    @BeforeEach
    void setUp(){
        user = User.builder()
                .email("test@test.com")
                .profileImage("ex")
                .nickname("ex")
                .isOnline(false)
                .type("kakao").build();
        userRepository.save(user);

        team = Team.builder()
                .user(user)
                .maxMember(15)
                .name("팀이름")
                .currentMember(1)
                .description("팀설명")
                .teamProfileImage("팀프로필사진")
                .build();
        teamRepository.save(team);

        scrum = Scrum.builder()
                .user(user)
                .team(team)
                .currentMember(1)
                .name("스크럼이름1")
                .subject("주제")
                .maxMember(15).build();
        scrumRepository.save(scrum);

        tem.clear();
    }

    @Test
    @DisplayName("삭제되지 않고 현재 팀에 속한 scurm 모두 조회")
    void 삭제되지_않고_팀에_속한_스크럼_모두조회(){
        // given
        // when
        List<Scrum> scrums = scrumRepository.findActiveScrumsByTeam(team);

        // then
        Assertions.assertFalse(scrums.isEmpty());
        Assertions.assertNotNull(scrums.get(0).getTeam());

        Scrum scrum = scrums.get(0);
        Assertions.assertNull(scrum.getDeleteDate());

        User fetchUser = scrum.getUser();
        Assertions.assertEquals("test@test.com", fetchUser.getEmail());

        Assertions.assertEquals("주제", scrum.getSubject());

    }

    @Test
    @DisplayName("삭제되지 않고 유저가 속한 scrum 모두 조회")
    void 삭제되지_않고_유저가_속한_스크럼_모두조회(){
        // given
        // when
        boolean result = scrumRepository.existsActiveScrumByUser(user);

        // then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("삭젝되지 않은 팀의 스크럼 조회")
    void 삭제되지_않은_팀의_스크럼_모두조회(){
        // given
        Scrum scrum1 = Scrum.builder()
                .user(user)
                .team(team)
                .currentMember(1)
                .name("스크럼이름2")
                .subject("주제")
                .maxMember(15).build();
        scrumRepository.save(scrum1);

        // when
        List<Scrum> activeScrumsByTeam = scrumRepository.findActiveScrumsByTeam(team);

        // then
        Assertions.assertFalse(activeScrumsByTeam.isEmpty());
        Assertions.assertEquals(activeScrumsByTeam.get(0).getName(), scrum.getName());
    }
}