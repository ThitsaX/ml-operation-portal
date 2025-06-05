package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import java.util.*;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLEndpointType is a Querydsl query type for CLEndpointType
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLEndpointType extends com.querydsl.sql.RelationalPathBase<CLEndpointType> {

    private static final long serialVersionUID = -1851332564;

    public static final CLEndpointType endpointType = new CLEndpointType("endpointType");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> endpointTypeId = createNumber("endpointTypeId", Integer.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<CLEndpointType> primary = createPrimaryKey(endpointTypeId);

    public final com.querydsl.sql.ForeignKey<CLParticipantEndpoint> _participantendpointEndpointtypeidForeign = createInvForeignKey(Arrays.asList(endpointTypeId, endpointTypeId), Arrays.asList("endpointTypeId", "endpointTypeId"));

    public CLEndpointType(String variable) {
        super(CLEndpointType.class, forVariable(variable), "null", "endpointType");
        addMetadata();
    }

    public CLEndpointType(String variable, String schema, String table) {
        super(CLEndpointType.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLEndpointType(String variable, String schema) {
        super(CLEndpointType.class, forVariable(variable), schema, "endpointType");
        addMetadata();
    }

    public CLEndpointType(Path<? extends CLEndpointType> path) {
        super(path.getType(), path.getMetadata(), "null", "endpointType");
        addMetadata();
    }

    public CLEndpointType(PathMetadata metadata) {
        super(CLEndpointType.class, metadata, "null", "endpointType");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(endpointTypeId, ColumnMetadata.named("endpointTypeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
    }

}

