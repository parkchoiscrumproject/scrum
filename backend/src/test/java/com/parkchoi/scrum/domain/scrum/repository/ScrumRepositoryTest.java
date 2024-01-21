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

    private User user;
    private Team team;
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

    }
    
    @Test
    @DisplayName("삭제되지 않고 현재 팀에 속한 scurm 조회")
    void 팀으로_삭제되지_않은_스크럼_조회_성공(){
        // given

        // when
        List<Scrum> scrums = scrumRepository.findByTeamWithScrumInfos(team).get();

        // then
        Assertions.assertNotNull(scrums);
        Assertions.assertNotNull(scrums.get(0).getScrumInfos());
    }

}