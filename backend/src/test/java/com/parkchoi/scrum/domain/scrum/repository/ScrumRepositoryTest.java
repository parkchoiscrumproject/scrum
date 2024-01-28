package com.parkchoi.scrum.domain.scrum.repository;

import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.scrum.entity.ScrumInfo;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.team.repository.TeamRepository;
import com.parkchoi.scrum.domain.user.entity.User;
import com.parkchoi.scrum.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ScrumRepositoryTest {

    @Autowired
    private ScrumRepository scrumRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private ScrumInfoRepository scrumInfoRepository;
    @Autowired
    private TestEntityManager tem;

    private Team team;
    private User user;
    private Scrum scrum;
    private ScrumInfo scrumInfo;

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
                .name("스크럼이름")
                .maxMember(15).build();
        scrumRepository.save(scrum);

        scrumInfo = ScrumInfo.builder()
                .isStart(false)
                .scrum(scrum)
                .subject("주제").build();
        scrumInfoRepository.save(scrumInfo);
        tem.clear();
    }

    @Test
    @DisplayName("삭제되지 않고 현재 팀에 속한 scurm 모두 조회")
    void 삭제되지_않고_팀에_속한_스크럼_모두조회(){
        // given
        // when
        List<Scrum> scrums = scrumRepository.findByTeamWithFetchJoinUserAndScrumInfoAndDeleteDateIsNull(team).get();

        // then
        Assertions.assertFalse(scrums.isEmpty());
        Assertions.assertNotNull(scrums.get(0).getScrumInfo());
        Assertions.assertNotNull(scrums.get(0).getTeam());

        Scrum scrum = scrums.get(0);
        Assertions.assertNull(scrum.getDeleteDate());

        User fetchUser = scrum.getUser();
        Assertions.assertEquals("test@test.com", fetchUser.getEmail());

        ScrumInfo fetchScrumInfo = scrum.getScrumInfo();
        Assertions.assertEquals("주제", fetchScrumInfo.getSubject());

    }

    @Test
    @DisplayName("삭제되지 않고 유저가 속한 scrum 모두 조회")
    void 삭제되지_않고_유저가_속한_스크럼_모두조회(){
        // given
        // when
        List<Scrum> scrums = scrumRepository.findByUserWithFetchJoinScrumInfoAndDeleteDateIsNullAndEndTimeIsNull(user).get();

        // then
        Scrum scrum = scrums.get(0);

        Assertions.assertNotNull(scrum.getUser());

        Assertions.assertNull(scrum.getScrumInfo().getEndTime());
        Assertions.assertNull(scrum.getDeleteDate());

    }
}