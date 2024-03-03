package com.parkchoi.scrum.domain.log.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserStatusMessageLog is a Querydsl query type for UserStatusMessageLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserStatusMessageLog extends EntityPathBase<UserStatusMessageLog> {

    private static final long serialVersionUID = 809590045L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserStatusMessageLog userStatusMessageLog = new QUserStatusMessageLog("userStatusMessageLog");

    public final DateTimePath<java.time.LocalDateTime> changeDate = createDateTime("changeDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath previousStatusMessage = createString("previousStatusMessage");

    public final com.parkchoi.scrum.domain.user.entity.QUser user;

    public QUserStatusMessageLog(String variable) {
        this(UserStatusMessageLog.class, forVariable(variable), INITS);
    }

    public QUserStatusMessageLog(Path<? extends UserStatusMessageLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserStatusMessageLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserStatusMessageLog(PathMetadata metadata, PathInits inits) {
        this(UserStatusMessageLog.class, metadata, inits);
    }

    public QUserStatusMessageLog(Class<? extends UserStatusMessageLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.parkchoi.scrum.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

