package com.parkchoi.scrum.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 685770609L;

    public static final QUser user = new QUser("user");

    public final com.parkchoi.scrum.util.api.QBaseTimeEntity _super = new com.parkchoi.scrum.util.api.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.parkchoi.scrum.domain.team.entity.InviteTeamList, com.parkchoi.scrum.domain.team.entity.QInviteTeamList> inviteTeamList = this.<com.parkchoi.scrum.domain.team.entity.InviteTeamList, com.parkchoi.scrum.domain.team.entity.QInviteTeamList>createList("inviteTeamList", com.parkchoi.scrum.domain.team.entity.InviteTeamList.class, com.parkchoi.scrum.domain.team.entity.QInviteTeamList.class, PathInits.DIRECT2);

    public final BooleanPath isOnline = createBoolean("isOnline");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath nickname = createString("nickname");

    public final StringPath profileImage = createString("profileImage");

    public final StringPath providerId = createString("providerId");

    public final StringPath statusMessage = createString("statusMessage");

    public final StringPath type = createString("type");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

