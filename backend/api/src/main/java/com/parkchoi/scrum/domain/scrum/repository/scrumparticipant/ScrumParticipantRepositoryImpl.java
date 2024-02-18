package com.parkchoi.scrum.domain.scrum.repository.scrumparticipant;

import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.parkchoi.scrum.domain.scrum.entity.QScrumParticipant.scrumParticipant;

@RequiredArgsConstructor
public class ScrumParticipantRepositoryImpl implements ScrumParticipantCustom {

    private final JPAQueryFactory queryFactory;

    // 유저가 스크럼에 참가하는지 판단
    @Override
    public Boolean existsEnterScrum(User user, Scrum scrum) {
        Integer result = queryFactory
                .selectOne()
                .from(scrumParticipant)
                .where(scrumParticipant.scrum.eq(scrum)
                        .and(scrumParticipant.user.eq(user)))
                .fetchFirst();

        return result != null;
    }
}
