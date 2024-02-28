package com.parkchoi.scrum.domain.team.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInviteTeamList is a Querydsl query type for InviteTeamList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInviteTeamList extends EntityPathBase<InviteTeamList> {

    private static final long serialVersionUID = -1011815428L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInviteTeamList inviteTeamList = new QInviteTeamList("inviteTeamList");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath participant = createBoolean("participant");

    public final QTeam team;

    public final com.parkchoi.scrum.domain.user.entity.QUser user;

    public QInviteTeamList(String variable) {
        this(InviteTeamList.class, forVariable(variable), INITS);
    }

    public QInviteTeamList(Path<? extends InviteTeamList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInviteTeamList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInviteTeamList(PathMetadata metadata, PathInits inits) {
        this(InviteTeamList.class, metadata, inits);
    }

    public QInviteTeamList(Class<? extends InviteTeamList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.team = inits.isInitialized("team") ? new QTeam(forProperty("team"), inits.get("team")) : null;
        this.user = inits.isInitialized("user") ? new com.parkchoi.scrum.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

