package com.thitsaworks.operation_portal.querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * DfspTblAudit is a Querydsl query type for DfspTblAudit
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class DfspTblAudit extends com.querydsl.sql.RelationalPathBase<DfspTblAudit> {

    private static final long serialVersionUID = -1297651509;

    public static final DfspTblAudit tblAudit = new DfspTblAudit("tbl_audit");

    public final NumberPath<Long> actionId = createNumber("actionId", Long.class);

    public final NumberPath<Long> auditId = createNumber("auditId", Long.class);

    public final NumberPath<Long> createdDate = createNumber("createdDate", Long.class);

    public final StringPath inputInfo = createString("inputInfo");

    public final StringPath outputInfo = createString("outputInfo");

    public final NumberPath<Long> participantId = createNumber("participantId", Long.class);

    public final NumberPath<Long> updatedDate = createNumber("updatedDate", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final com.querydsl.sql.PrimaryKey<DfspTblAudit> primary = createPrimaryKey(auditId);

    public DfspTblAudit(String variable) {
        super(DfspTblAudit.class, forVariable(variable), "null", "tbl_audit");
        addMetadata();
    }

    public DfspTblAudit(String variable, String schema, String table) {
        super(DfspTblAudit.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public DfspTblAudit(String variable, String schema) {
        super(DfspTblAudit.class, forVariable(variable), schema, "tbl_audit");
        addMetadata();
    }

    public DfspTblAudit(Path<? extends DfspTblAudit> path) {
        super(path.getType(), path.getMetadata(), "null", "tbl_audit");
        addMetadata();
    }

    public DfspTblAudit(PathMetadata metadata) {
        super(DfspTblAudit.class, metadata, "null", "tbl_audit");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(actionId, ColumnMetadata.named("action_id").withIndex(2).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(auditId, ColumnMetadata.named("audit_id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(createdDate, ColumnMetadata.named("created_date").withIndex(7).ofType(Types.BIGINT).withSize(19));
        addMetadata(inputInfo, ColumnMetadata.named("input_info").withIndex(5).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(outputInfo, ColumnMetadata.named("output_info").withIndex(6).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(participantId, ColumnMetadata.named("participant_id").withIndex(4).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(updatedDate, ColumnMetadata.named("updated_date").withIndex(8).ofType(Types.BIGINT).withSize(19));
        addMetadata(userId, ColumnMetadata.named("user_id").withIndex(3).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

