package com.thitsaworks.operation_portal.querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * DfspTblAction is a Querydsl query type for DfspTblAction
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class DfspTblAction extends com.querydsl.sql.RelationalPathBase<DfspTblAction> {

    private static final long serialVersionUID = -1588637882;

    public static final DfspTblAction tblAction = new DfspTblAction("tbl_action");

    public final NumberPath<Long> actionId = createNumber("actionId", Long.class);

    public final NumberPath<Long> createdDate = createNumber("createdDate", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> updatedDate = createNumber("updatedDate", Long.class);

    public final com.querydsl.sql.PrimaryKey<DfspTblAction> primary = createPrimaryKey(actionId);

    public DfspTblAction(String variable) {
        super(DfspTblAction.class, forVariable(variable), "null", "tbl_action");
        addMetadata();
    }

    public DfspTblAction(String variable, String schema, String table) {
        super(DfspTblAction.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public DfspTblAction(String variable, String schema) {
        super(DfspTblAction.class, forVariable(variable), schema, "tbl_action");
        addMetadata();
    }

    public DfspTblAction(Path<? extends DfspTblAction> path) {
        super(path.getType(), path.getMetadata(), "null", "tbl_action");
        addMetadata();
    }

    public DfspTblAction(PathMetadata metadata) {
        super(DfspTblAction.class, metadata, "null", "tbl_action");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(actionId, ColumnMetadata.named("action_id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(createdDate, ColumnMetadata.named("created_date").withIndex(3).ofType(Types.BIGINT).withSize(19));
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(150));
        addMetadata(updatedDate, ColumnMetadata.named("updated_date").withIndex(4).ofType(Types.BIGINT).withSize(19));
    }

}

