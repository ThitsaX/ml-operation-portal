package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLToken is a Querydsl query type for CLToken
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLToken extends com.querydsl.sql.RelationalPathBase<CLToken> {

    private static final long serialVersionUID = -1710167332;

    public static final CLToken token = new CLToken("token");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final NumberPath<Long> expiration = createNumber("expiration", Long.class);

    public final NumberPath<Integer> participantId = createNumber("participantId", Integer.class);

    public final NumberPath<Integer> tokenId = createNumber("tokenId", Integer.class);

    public final StringPath value = createString("value");

    public final com.querydsl.sql.PrimaryKey<CLToken> primary = createPrimaryKey(tokenId);

    public final com.querydsl.sql.ForeignKey<CLParticipant> tokenParticipantidForeign = createForeignKey(participantId, "participantId");

    public CLToken(String variable) {
        super(CLToken.class, forVariable(variable), "null", "token");
        addMetadata();
    }

    public CLToken(String variable, String schema, String table) {
        super(CLToken.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLToken(String variable, String schema) {
        super(CLToken.class, forVariable(variable), schema, "token");
        addMetadata();
    }

    public CLToken(Path<? extends CLToken> path) {
        super(path.getType(), path.getMetadata(), "null", "token");
        addMetadata();
    }

    public CLToken(PathMetadata metadata) {
        super(CLToken.class, metadata, "null", "token");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(expiration, ColumnMetadata.named("expiration").withIndex(4).ofType(Types.BIGINT).withSize(19));
        addMetadata(participantId, ColumnMetadata.named("participantId").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(tokenId, ColumnMetadata.named("tokenId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(3).ofType(Types.VARCHAR).withSize(256).notNull());
    }

}

