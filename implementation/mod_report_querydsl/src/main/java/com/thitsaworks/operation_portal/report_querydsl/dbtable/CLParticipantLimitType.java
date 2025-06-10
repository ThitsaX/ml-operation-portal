package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import java.util.*;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLParticipantLimitType is a Querydsl query type for CLParticipantLimitType
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLParticipantLimitType extends com.querydsl.sql.RelationalPathBase<CLParticipantLimitType> {

    private static final long serialVersionUID = 952311903;

    public static final CLParticipantLimitType participantLimitType = new CLParticipantLimitType("participantLimitType");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> participantLimitTypeId = createNumber("participantLimitTypeId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLParticipantLimitType> primary = createPrimaryKey(participantLimitTypeId);

    public final com.querydsl.sql.ForeignKey<CLParticipantLimit> _participantlimitParticipantlimittypeidForeign = createInvForeignKey(Arrays.asList(participantLimitTypeId, participantLimitTypeId), Arrays.asList("participantLimitTypeId", "participantLimitTypeId"));

    public CLParticipantLimitType(String variable) {
        super(CLParticipantLimitType.class, forVariable(variable), "null", "participantLimitType");
        addMetadata();
    }

    public CLParticipantLimitType(String variable, String schema, String table) {
        super(CLParticipantLimitType.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLParticipantLimitType(String variable, String schema) {
        super(CLParticipantLimitType.class, forVariable(variable), schema, "participantLimitType");
        addMetadata();
    }

    public CLParticipantLimitType(Path<? extends CLParticipantLimitType> path) {
        super(path.getType(), path.getMetadata(), "null", "participantLimitType");
        addMetadata();
    }

    public CLParticipantLimitType(PathMetadata metadata) {
        super(CLParticipantLimitType.class, metadata, "null", "participantLimitType");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(participantLimitTypeId, ColumnMetadata.named("participantLimitTypeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

