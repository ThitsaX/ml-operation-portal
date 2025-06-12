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
 * CLSettlementInterchange is a Querydsl query type for CLSettlementInterchange
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementInterchange extends com.querydsl.sql.RelationalPathBase<CLSettlementInterchange> {

    private static final long serialVersionUID = 1099131270;

    public static final CLSettlementInterchange settlementInterchange = new CLSettlementInterchange("settlementInterchange");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> settlementInterchangeId = createNumber("settlementInterchangeId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLSettlementInterchange> primary = createPrimaryKey(settlementInterchangeId);

    public final com.querydsl.sql.ForeignKey<CLSettlementModel> _settlementmodelSettlementinterchangeidForeign = createInvForeignKey(Arrays.asList(settlementInterchangeId, settlementInterchangeId), Arrays.asList("settlementInterchangeId", "settlementInterchangeId"));

    public CLSettlementInterchange(String variable) {
        super(CLSettlementInterchange.class, forVariable(variable), "null", "settlementInterchange");
        addMetadata();
    }

    public CLSettlementInterchange(String variable, String schema, String table) {
        super(CLSettlementInterchange.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementInterchange(String variable, String schema) {
        super(CLSettlementInterchange.class, forVariable(variable), schema, "settlementInterchange");
        addMetadata();
    }

    public CLSettlementInterchange(Path<? extends CLSettlementInterchange> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementInterchange");
        addMetadata();
    }

    public CLSettlementInterchange(PathMetadata metadata) {
        super(CLSettlementInterchange.class, metadata, "null", "settlementInterchange");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(settlementInterchangeId, ColumnMetadata.named("settlementInterchangeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

