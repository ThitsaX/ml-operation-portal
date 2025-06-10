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
 * CLBulkProcessingState is a Querydsl query type for CLBulkProcessingState
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLBulkProcessingState extends com.querydsl.sql.RelationalPathBase<CLBulkProcessingState> {

    private static final long serialVersionUID = -677486225;

    public static final CLBulkProcessingState bulkProcessingState = new CLBulkProcessingState("bulkProcessingState");

    public final NumberPath<Integer> bulkProcessingStateId = createNumber("bulkProcessingStateId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<CLBulkProcessingState> primary = createPrimaryKey(bulkProcessingStateId);

    public final com.querydsl.sql.ForeignKey<CLBulkTransferAssociation> _bulktransferassociationBulkprocessingstateidForeign = createInvForeignKey(Arrays.asList(bulkProcessingStateId, bulkProcessingStateId), Arrays.asList("bulkProcessingStateId", "bulkProcessingStateId"));

    public CLBulkProcessingState(String variable) {
        super(CLBulkProcessingState.class, forVariable(variable), "null", "bulkProcessingState");
        addMetadata();
    }

    public CLBulkProcessingState(String variable, String schema, String table) {
        super(CLBulkProcessingState.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLBulkProcessingState(String variable, String schema) {
        super(CLBulkProcessingState.class, forVariable(variable), schema, "bulkProcessingState");
        addMetadata();
    }

    public CLBulkProcessingState(Path<? extends CLBulkProcessingState> path) {
        super(path.getType(), path.getMetadata(), "null", "bulkProcessingState");
        addMetadata();
    }

    public CLBulkProcessingState(PathMetadata metadata) {
        super(CLBulkProcessingState.class, metadata, "null", "bulkProcessingState");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bulkProcessingStateId, ColumnMetadata.named("bulkProcessingStateId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
    }

}

