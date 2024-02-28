package com.parkchoi.scrum.domain.log.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserLoginLog is a Querydsl query type for UserLoginLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserLoginLog extends EntityPathBase<UserLoginLog> {

    private static final long serialVersionUID = -1352113431L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserLoginLog userLoginLog = new QUserLoginLog("userLoginLog");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isLoginSuccess = createBoolean("isLoginSuccess");

    public final StringPath loginIp = createString("loginIp");

    public final DateTimePath<java.time.LocalDateTime> loginTime = createDateTime("loginTime", java.time.LocalDateTime.class);

    public final com.parkchoi.scrum.domain.user.entity.QUser user;

    public QUserLoginLog(String variable) {
        this(UserLoginLog.class, forVariable(variable), INITS);
    }

    public QUserLoginLog(Path<? extends UserLoginLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserLoginLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserLoginLog(PathMetadata metadata, PathInits inits) {
        this(UserLoginLog.class, metadata, inits);
    }

    public QUserLoginLog(Class<? extends UserLoginLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.parkchoi.scrum.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

