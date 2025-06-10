package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLEvent is a Querydsl query type for CLEvent
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLEvent extends com.querydsl.sql.RelationalPathBase<CLEvent> {

    private static final long serialVersionUID = -1723817091;

    public static final CLEvent event = new CLEvent("event");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> eventId = createNumber("eventId", Integer.class);

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<CLEvent> primary = createPrimaryKey(eventId);

    public CLEvent(String variable) {
        super(CLEvent.class, forVariable(variable), "null", "event");
        addMetadata();
    }

    public CLEvent(String variable, String schema, String table) {
        super(CLEvent.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLEvent(String variable, String schema) {
        super(CLEvent.class, forVariable(variable), schema, "event");
        addMetadata();
    }

    public CLEvent(Path<? extends CLEvent> path) {
        super(path.getType(), path.getMetadata(), "null", "event");
        addMetadata();
    }

    public CLEvent(PathMetadata metadata) {
        super(CLEvent.class, metadata, "null", "event");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(eventId, ColumnMetadata.named("eventId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(128).notNull());
    }

}

