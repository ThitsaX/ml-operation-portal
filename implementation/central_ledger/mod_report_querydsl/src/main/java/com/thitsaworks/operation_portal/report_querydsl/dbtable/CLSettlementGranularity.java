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
 * CLSettlementGranularity is a Querydsl query type for CLSettlementGranularity
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementGranularity extends com.querydsl.sql.RelationalPathBase<CLSettlementGranularity> {

    private static final long serialVersionUID = 1013543752;

    public static final CLSettlementGranularity settlementGranularity = new CLSettlementGranularity("settlementGranularity");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> settlementGranularityId = createNumber("settlementGranularityId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLSettlementGranularity> primary = createPrimaryKey(settlementGranularityId);

    public final com.querydsl.sql.ForeignKey<CLSettlementModel> _settlementmodelSettlementgranularityidForeign = createInvForeignKey(Arrays.asList(settlementGranularityId, settlementGranularityId), Arrays.asList("settlementGranularityId", "settlementGranularityId"));

    public CLSettlementGranularity(String variable) {
        super(CLSettlementGranularity.class, forVariable(variable), "null", "settlementGranularity");
        addMetadata();
    }

    public CLSettlementGranularity(String variable, String schema, String table) {
        super(CLSettlementGranularity.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementGranularity(String variable, String schema) {
        super(CLSettlementGranularity.class, forVariable(variable), schema, "settlementGranularity");
        addMetadata();
    }

    public CLSettlementGranularity(Path<? extends CLSettlementGranularity> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementGranularity");
        addMetadata();
    }

    public CLSettlementGranularity(PathMetadata metadata) {
        super(CLSettlementGranularity.class, metadata, "null", "settlementGranularity");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(settlementGranularityId, ColumnMetadata.named("settlementGranularityId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

