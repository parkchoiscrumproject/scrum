package com.parkchoi.scrum.domain.log.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserNicknameLog is a Querydsl query type for UserNicknameLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserNicknameLog extends EntityPathBase<UserNicknameLog> {

    private static final long serialVersionUID = 1997110632L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserNicknameLog userNicknameLog = new QUserNicknameLog("userNicknameLog");

    public final DateTimePath<java.time.LocalDateTime> changeDate = createDateTime("changeDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath previousNickname = createString("previousNickname");

    public final com.parkchoi.scrum.domain.user.entity.QUser user;

    public QUserNicknameLog(String variable) {
        this(UserNicknameLog.class, forVariable(variable), INITS);
    }

    public QUserNicknameLog(Path<? extends UserNicknameLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserNicknameLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserNicknameLog(PathMetadata metadata, PathInits inits) {
        this(UserNicknameLog.class, metadata, inits);
    }

    public QUserNicknameLog(Class<? extends UserNicknameLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.parkchoi.scrum.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

