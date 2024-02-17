package com.parkchoi.scrum.domain.scrum.repository.scrum;

import com.parkchoi.scrum.domain.scrum.entity.Scrum;
import com.parkchoi.scrum.domain.team.entity.Team;
import com.parkchoi.scrum.domain.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.parkchoi.scrum.domain.scrum.entity.QScrum.scrum;

@RequiredArgsConstructor
public class ScrumRepositoryImpl implements ScrumRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    // 현재 유저가 리더인 삭제X, 종료X 스크럼 존재 여부
    @Override
    public Boolean existsActiveScrumByUser(User user) {
        Integer result = queryFactory
                .selectOne()
                .from(scrum)
                .where(scrum.user.eq(user)
                        .and(scrum.deleteDate.isNull())
                        .and(scrum.endTime.isNull()))
                .fetchFirst();

        return result != null;
    }

    // 삭제되지 않은 현재 팀의 모든 스크럼 조회
    @Override
    public List<Scrum> findActiveScrumsByTeam(Team team) {
        return queryFactory
                .selectFrom(scrum)
                .join(scrum.user).fetchJoin()
                .where(scrum.team.eq(team)
                        .and(scrum.deleteDate.isNull()))
                .fetch();
    }

    // 종료되지 않고 삭제되지 않은 스크럼 조회
    @Override
    public Optional<Scrum> findActiveScrumByScrumId(Long scrumId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(scrum)
                .where(scrum.id.eq(scrumId)
                        .and(scrum.deleteDate.isNull())
                        .and(scrum.endTime.isNull()))
                .fetchOne());
    }

    // 스크럼 조건 검색 페이지네이션
    @Override
    public Page<Scrum> searchScrumWithPagination(String type, String key, Pageable pageable) {

        BooleanExpression searchCondition = createSearchCondition(type, key);

        List<Scrum> scrums = queryFactory
                .selectFrom(scrum)
                .where(searchCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(scrum)
                .where(searchCondition)
                .fetchCount();

        return new PageImpl<>(scrums, pageable, total);
    }

    // 제목 판단(검색)
    private BooleanExpression titleContains(String title){
        if(title == null || title.trim().isEmpty()){
            return null;
        }
        return scrum.name.containsIgnoreCase(title);
    }

    // 리더 닉네임 판단(검색)
    private BooleanExpression leaderNameContains(String leaderName){
        if(leaderName == null || leaderName.trim().isEmpty()){
            return null;
        }
        return scrum.user.nickname.containsIgnoreCase(leaderName);
    }

    // 검색 조건 쿼리 생성
    private BooleanExpression createSearchCondition(String type, String key){
        if("leaderName".equals(type)){
            return leaderNameContains(key);
        }else if("title".equals(type)){
            return titleContains(key);
        }
        return null;
    }
}
