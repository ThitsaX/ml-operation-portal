package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLParticipantEndpoint is a Querydsl query type for CLParticipantEndpoint
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLParticipantEndpoint extends com.querydsl.sql.RelationalPathBase<CLParticipantEndpoint> {

    private static final long serialVersionUID = -1055394421;

    public static final CLParticipantEndpoint participantEndpoint = new CLParticipantEndpoint("participantEndpoint");

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> endpointTypeId = createNumber("endpointTypeId", Integer.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final NumberPath<Integer> participantEndpointId = createNumber("participantEndpointId", Integer.class);

    public final NumberPath<Integer> participantId = createNumber("participantId", Integer.class);

    public final StringPath value = createString("value");

    public final com.querydsl.sql.PrimaryKey<CLParticipantEndpoint> primary = createPrimaryKey(participantEndpointId);

    public final com.querydsl.sql.ForeignKey<CLEndpointType> participantendpointEndpointtypeidForeign = createForeignKey(endpointTypeId, "endpointTypeId");

    public final com.querydsl.sql.ForeignKey<CLParticipant> participantendpointParticipantidForeign = createForeignKey(participantId, "participantId");

    public CLParticipantEndpoint(String variable) {
        super(CLParticipantEndpoint.class, forVariable(variable), "null", "participantEndpoint");
        addMetadata();
    }

    public CLParticipantEndpoint(String variable, String schema, String table) {
        super(CLParticipantEndpoint.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLParticipantEndpoint(String variable, String schema) {
        super(CLParticipantEndpoint.class, forVariable(variable), schema, "participantEndpoint");
        addMetadata();
    }

    public CLParticipantEndpoint(Path<? extends CLParticipantEndpoint> path) {
        super(path.getType(), path.getMetadata(), "null", "participantEndpoint");
        addMetadata();
    }

    public CLParticipantEndpoint(PathMetadata metadata) {
        super(CLParticipantEndpoint.class, metadata, "null", "participantEndpoint");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdBy, ColumnMetadata.named("createdBy").withIndex(7).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(6).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(endpointTypeId, ColumnMetadata.named("endpointTypeId").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(5).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(participantEndpointId, ColumnMetadata.named("participantEndpointId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(participantId, ColumnMetadata.named("participantId").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(4).ofType(Types.VARCHAR).withSize(512).notNull());
    }

}

