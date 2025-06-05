package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSegment is a Querydsl query type for CLSegment
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSegment extends com.querydsl.sql.RelationalPathBase<CLSegment> {

    private static final long serialVersionUID = 324412246;

    public static final CLSegment segment = new CLSegment("segment");

    public final DateTimePath<java.sql.Timestamp> changedDate = createDateTime("changedDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> enumeration = createNumber("enumeration", Integer.class);

    public final NumberPath<Integer> segmentId = createNumber("segmentId", Integer.class);

    public final StringPath segmentType = createString("segmentType");

    public final StringPath tableName = createString("tableName");

    public final NumberPath<Long> value = createNumber("value", Long.class);

    public final com.querydsl.sql.PrimaryKey<CLSegment> primary = createPrimaryKey(segmentId);

    public CLSegment(String variable) {
        super(CLSegment.class, forVariable(variable), "null", "segment");
        addMetadata();
    }

    public CLSegment(String variable, String schema, String table) {
        super(CLSegment.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSegment(String variable, String schema) {
        super(CLSegment.class, forVariable(variable), schema, "segment");
        addMetadata();
    }

    public CLSegment(Path<? extends CLSegment> path) {
        super(path.getType(), path.getMetadata(), "null", "segment");
        addMetadata();
    }

    public CLSegment(PathMetadata metadata) {
        super(CLSegment.class, metadata, "null", "segment");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(changedDate, ColumnMetadata.named("changedDate").withIndex(6).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(enumeration, ColumnMetadata.named("enumeration").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(segmentId, ColumnMetadata.named("segmentId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(segmentType, ColumnMetadata.named("segmentType").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(tableName, ColumnMetadata.named("tableName").withIndex(4).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(5).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

