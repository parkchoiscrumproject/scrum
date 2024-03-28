package com.parkchoi.scrum.domain.scrum.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScrum is a Querydsl query type for Scrum
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScrum extends EntityPathBase<Scrum> {

    private static final long serialVersionUID = 1207684609L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScrum scrum = new QScrum("scrum");

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> currentMember = createNumber("currentMember", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> deleteDate = createDateTime("deleteDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isStart = createBoolean("isStart");

    public final NumberPath<Integer> maxMember = createNumber("maxMember", Integer.class);

    public final StringPath name = createString("name");

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public final StringPath subject = createString("subject");

    public final com.parkchoi.scrum.domain.team.entity.QTeam team;

    public final com.parkchoi.scrum.domain.user.entity.QUser user;

    public QScrum(String variable) {
        this(Scrum.class, forVariable(variable), INITS);
    }

    public QScrum(Path<? extends Scrum> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScrum(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScrum(PathMetadata metadata, PathInits inits) {
        this(Scrum.class, metadata, inits);
    }

    public QScrum(Class<? extends Scrum> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.team = inits.isInitialized("team") ? new com.parkchoi.scrum.domain.team.entity.QTeam(forProperty("team"), inits.get("team")) : null;
        this.user = inits.isInitialized("user") ? new com.parkchoi.scrum.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

