package com.parkchoi.scrum.domain.user.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{
    private JPAQueryFactory jpaQueryFactory;
}
