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
 * CLSettlementDelay is a Querydsl query type for CLSettlementDelay
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementDelay extends com.querydsl.sql.RelationalPathBase<CLSettlementDelay> {

    private static final long serialVersionUID = -931904771;

    public static final CLSettlementDelay settlementDelay = new CLSettlementDelay("settlementDelay");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> settlementDelayId = createNumber("settlementDelayId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLSettlementDelay> primary = createPrimaryKey(settlementDelayId);

    public final com.querydsl.sql.ForeignKey<CLSettlementModel> _settlementmodelSettlementdelayidForeign = createInvForeignKey(Arrays.asList(settlementDelayId, settlementDelayId), Arrays.asList("settlementDelayId", "settlementDelayId"));

    public CLSettlementDelay(String variable) {
        super(CLSettlementDelay.class, forVariable(variable), "null", "settlementDelay");
        addMetadata();
    }

    public CLSettlementDelay(String variable, String schema, String table) {
        super(CLSettlementDelay.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementDelay(String variable, String schema) {
        super(CLSettlementDelay.class, forVariable(variable), schema, "settlementDelay");
        addMetadata();
    }

    public CLSettlementDelay(Path<? extends CLSettlementDelay> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementDelay");
        addMetadata();
    }

    public CLSettlementDelay(PathMetadata metadata) {
        super(CLSettlementDelay.class, metadata, "null", "settlementDelay");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(settlementDelayId, ColumnMetadata.named("settlementDelayId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

