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
 * CLContactType is a Querydsl query type for CLContactType
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLContactType extends com.querydsl.sql.RelationalPathBase<CLContactType> {

    private static final long serialVersionUID = -1515400675;

    public static final CLContactType contactType = new CLContactType("contactType");

    public final NumberPath<Integer> contactTypeId = createNumber("contactTypeId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<CLContactType> primary = createPrimaryKey(contactTypeId);

    public final com.querydsl.sql.ForeignKey<CLParticipantContact> _participantcontactContacttypeidForeign = createInvForeignKey(Arrays.asList(contactTypeId, contactTypeId), Arrays.asList("contactTypeId", "contactTypeId"));

    public CLContactType(String variable) {
        super(CLContactType.class, forVariable(variable), "null", "contactType");
        addMetadata();
    }

    public CLContactType(String variable, String schema, String table) {
        super(CLContactType.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLContactType(String variable, String schema) {
        super(CLContactType.class, forVariable(variable), schema, "contactType");
        addMetadata();
    }

    public CLContactType(Path<? extends CLContactType> path) {
        super(path.getType(), path.getMetadata(), "null", "contactType");
        addMetadata();
    }

    public CLContactType(PathMetadata metadata) {
        super(CLContactType.class, metadata, "null", "contactType");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(contactTypeId, ColumnMetadata.named("contactTypeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
    }

}

