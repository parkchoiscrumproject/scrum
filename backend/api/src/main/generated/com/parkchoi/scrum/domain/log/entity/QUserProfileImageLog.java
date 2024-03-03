package com.parkchoi.scrum.domain.log.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserProfileImageLog is a Querydsl query type for UserProfileImageLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserProfileImageLog extends EntityPathBase<UserProfileImageLog> {

    private static final long serialVersionUID = -1345297116L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserProfileImageLog userProfileImageLog = new QUserProfileImageLog("userProfileImageLog");

    public final DateTimePath<java.time.LocalDateTime> changeDate = createDateTime("changeDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath previousProfileImage = createString("previousProfileImage");

    public final com.parkchoi.scrum.domain.user.entity.QUser user;

    public QUserProfileImageLog(String variable) {
        this(UserProfileImageLog.class, forVariable(variable), INITS);
    }

    public QUserProfileImageLog(Path<? extends UserProfileImageLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserProfileImageLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserProfileImageLog(PathMetadata metadata, PathInits inits) {
        this(UserProfileImageLog.class, metadata, inits);
    }

    public QUserProfileImageLog(Class<? extends UserProfileImageLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.parkchoi.scrum.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

