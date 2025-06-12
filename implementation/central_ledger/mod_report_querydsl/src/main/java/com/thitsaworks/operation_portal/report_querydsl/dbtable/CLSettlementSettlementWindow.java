package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSettlementSettlementWindow is a Querydsl query type for CLSettlementSettlementWindow
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementSettlementWindow extends com.querydsl.sql.RelationalPathBase<CLSettlementSettlementWindow> {

    private static final long serialVersionUID = -1703824193;

    public static final CLSettlementSettlementWindow settlementSettlementWindow = new CLSettlementSettlementWindow("settlementSettlementWindow");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final NumberPath<Long> settlementId = createNumber("settlementId", Long.class);

    public final NumberPath<Long> settlementSettlementWindowId = createNumber("settlementSettlementWindowId", Long.class);

    public final NumberPath<Long> settlementWindowId = createNumber("settlementWindowId", Long.class);

    public final com.querydsl.sql.PrimaryKey<CLSettlementSettlementWindow> primary = createPrimaryKey(settlementSettlementWindowId);

    public final com.querydsl.sql.ForeignKey<CLSettlement> settlementsettlementwindowSettlementidForeign = createForeignKey(settlementId, "settlementId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindow> settlementsettlementwindowSettlementwindowidForeign = createForeignKey(settlementWindowId, "settlementWindowId");

    public CLSettlementSettlementWindow(String variable) {
        super(CLSettlementSettlementWindow.class, forVariable(variable), "null", "settlementSettlementWindow");
        addMetadata();
    }

    public CLSettlementSettlementWindow(String variable, String schema, String table) {
        super(CLSettlementSettlementWindow.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementSettlementWindow(String variable, String schema) {
        super(CLSettlementSettlementWindow.class, forVariable(variable), schema, "settlementSettlementWindow");
        addMetadata();
    }

    public CLSettlementSettlementWindow(Path<? extends CLSettlementSettlementWindow> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementSettlementWindow");
        addMetadata();
    }

    public CLSettlementSettlementWindow(PathMetadata metadata) {
        super(CLSettlementSettlementWindow.class, metadata, "null", "settlementSettlementWindow");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(settlementId, ColumnMetadata.named("settlementId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementSettlementWindowId, ColumnMetadata.named("settlementSettlementWindowId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementWindowId, ColumnMetadata.named("settlementWindowId").withIndex(3).ofType(Types.BIGINT).withSize(20).notNull());
    }

}

