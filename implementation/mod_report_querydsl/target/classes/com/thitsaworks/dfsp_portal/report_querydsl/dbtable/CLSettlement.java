package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSettlement is a Querydsl query type for CLSettlement
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlement extends com.querydsl.sql.RelationalPathBase<CLSettlement> {

    private static final long serialVersionUID = -114921946;

    public static final CLSettlement settlement = new CLSettlement("settlement");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final NumberPath<Long> currentStateChangeId = createNumber("currentStateChangeId", Long.class);

    public final StringPath reason = createString("reason");

    public final NumberPath<Long> settlementId = createNumber("settlementId", Long.class);

    public final NumberPath<Integer> settlementModelId = createNumber("settlementModelId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLSettlement> primary = createPrimaryKey(settlementId);

    public final com.querydsl.sql.ForeignKey<CLSettlementStateChange> settlementCurrentstatechangeidForeign = createForeignKey(currentStateChangeId, "settlementStateChangeId");

    public final com.querydsl.sql.ForeignKey<CLSettlementModel> settlementSettlementmodelidForeign = createForeignKey(settlementModelId, "settlementModelId");

    public final com.querydsl.sql.ForeignKey<CLSettlementContentAggregation> _settlementcontentaggregationSettlementidForeign = createInvForeignKey(settlementId, "settlementId");

    public final com.querydsl.sql.ForeignKey<CLSettlementParticipantCurrency> _settlementparticipantcurrencySettlementidForeign = createInvForeignKey(settlementId, "settlementId");

    public final com.querydsl.sql.ForeignKey<CLSettlementSettlementWindow> _settlementsettlementwindowSettlementidForeign = createInvForeignKey(settlementId, "settlementId");

    public final com.querydsl.sql.ForeignKey<CLSettlementStateChange> _settlementstatechangeSettlementidForeign = createInvForeignKey(settlementId, "settlementId");

    public final com.querydsl.sql.ForeignKey<CLSettlementTransferParticipant> _settlementtransferparticipantSettlementidForeign = createInvForeignKey(settlementId, "settlementId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowContent> _settlementwindowcontentSettlementidForeign = createInvForeignKey(settlementId, "settlementId");

    public CLSettlement(String variable) {
        super(CLSettlement.class, forVariable(variable), "null", "settlement");
        addMetadata();
    }

    public CLSettlement(String variable, String schema, String table) {
        super(CLSettlement.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlement(String variable, String schema) {
        super(CLSettlement.class, forVariable(variable), schema, "settlement");
        addMetadata();
    }

    public CLSettlement(Path<? extends CLSettlement> path) {
        super(path.getType(), path.getMetadata(), "null", "settlement");
        addMetadata();
    }

    public CLSettlement(PathMetadata metadata) {
        super(CLSettlement.class, metadata, "null", "settlement");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(currentStateChangeId, ColumnMetadata.named("currentStateChangeId").withIndex(4).ofType(Types.BIGINT).withSize(20));
        addMetadata(reason, ColumnMetadata.named("reason").withIndex(2).ofType(Types.VARCHAR).withSize(512));
        addMetadata(settlementId, ColumnMetadata.named("settlementId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementModelId, ColumnMetadata.named("settlementModelId").withIndex(5).ofType(Types.INTEGER).withSize(10));
    }

}

