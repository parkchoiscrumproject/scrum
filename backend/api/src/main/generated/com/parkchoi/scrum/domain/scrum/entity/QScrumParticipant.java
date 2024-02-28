package com.parkchoi.scrum.domain.scrum.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QScrumParticipant is a Querydsl query type for ScrumParticipant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QScrumParticipant extends EntityPathBase<ScrumParticipant> {

    private static final long serialVersionUID = 152088178L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QScrumParticipant scrumParticipant = new QScrumParticipant("scrumParticipant");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QScrum scrum;

    public final com.parkchoi.scrum.domain.user.entity.QUser user;

    public QScrumParticipant(String variable) {
        this(ScrumParticipant.class, forVariable(variable), INITS);
    }

    public QScrumParticipant(Path<? extends ScrumParticipant> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QScrumParticipant(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QScrumParticipant(PathMetadata metadata, PathInits inits) {
        this(ScrumParticipant.class, metadata, inits);
    }

    public QScrumParticipant(Class<? extends ScrumParticipant> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.scrum = inits.isInitialized("scrum") ? new QScrum(forProperty("scrum"), inits.get("scrum")) : null;
        this.user = inits.isInitialized("user") ? new com.parkchoi.scrum.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

