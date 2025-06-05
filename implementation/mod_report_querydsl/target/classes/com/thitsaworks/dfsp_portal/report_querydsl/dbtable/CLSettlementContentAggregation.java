package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSettlementContentAggregation is a Querydsl query type for CLSettlementContentAggregation
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementContentAggregation extends com.querydsl.sql.RelationalPathBase<CLSettlementContentAggregation> {

    private static final long serialVersionUID = -1279331985;

    public static final CLSettlementContentAggregation settlementContentAggregation = new CLSettlementContentAggregation("settlementContentAggregation");

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath currentStateId = createString("currentStateId");

    public final NumberPath<Integer> ledgerEntryTypeId = createNumber("ledgerEntryTypeId", Integer.class);

    public final NumberPath<Integer> participantCurrencyId = createNumber("participantCurrencyId", Integer.class);

    public final NumberPath<Long> settlementContentAggregationId = createNumber("settlementContentAggregationId", Long.class);

    public final NumberPath<Long> settlementId = createNumber("settlementId", Long.class);

    public final NumberPath<Long> settlementWindowContentId = createNumber("settlementWindowContentId", Long.class);

    public final NumberPath<Integer> transferParticipantRoleTypeId = createNumber("transferParticipantRoleTypeId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLSettlementContentAggregation> primary = createPrimaryKey(settlementContentAggregationId);

    public final com.querydsl.sql.ForeignKey<CLTransferParticipantRoleType> scaTransferparticipantroletypeidForeign = createForeignKey(transferParticipantRoleTypeId, "transferParticipantRoleTypeId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowState> settlementcontentaggregationCurrentstateidForeign = createForeignKey(currentStateId, "settlementWindowStateId");

    public final com.querydsl.sql.ForeignKey<CLLedgerEntryType> settlementcontentaggregationLedgerentrytypeidForeign = createForeignKey(ledgerEntryTypeId, "ledgerEntryTypeId");

    public final com.querydsl.sql.ForeignKey<CLParticipantCurrency> settlementcontentaggregationParticipantcurrencyidForeign = createForeignKey(participantCurrencyId, "participantCurrencyId");

    public final com.querydsl.sql.ForeignKey<CLSettlement> settlementcontentaggregationSettlementidForeign = createForeignKey(settlementId, "settlementId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowContent> settlementcontentaggregationSettlementwindowcontentidForeign = createForeignKey(settlementWindowContentId, "settlementWindowContentId");

    public CLSettlementContentAggregation(String variable) {
        super(CLSettlementContentAggregation.class, forVariable(variable), "null", "settlementContentAggregation");
        addMetadata();
    }

    public CLSettlementContentAggregation(String variable, String schema, String table) {
        super(CLSettlementContentAggregation.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementContentAggregation(String variable, String schema) {
        super(CLSettlementContentAggregation.class, forVariable(variable), schema, "settlementContentAggregation");
        addMetadata();
    }

    public CLSettlementContentAggregation(Path<? extends CLSettlementContentAggregation> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementContentAggregation");
        addMetadata();
    }

    public CLSettlementContentAggregation(PathMetadata metadata) {
        super(CLSettlementContentAggregation.class, metadata, "null", "settlementContentAggregation");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(amount, ColumnMetadata.named("amount").withIndex(6).ofType(Types.DECIMAL).withSize(18).withDigits(4).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(7).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(currentStateId, ColumnMetadata.named("currentStateId").withIndex(8).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(ledgerEntryTypeId, ColumnMetadata.named("ledgerEntryTypeId").withIndex(5).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(participantCurrencyId, ColumnMetadata.named("participantCurrencyId").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(settlementContentAggregationId, ColumnMetadata.named("settlementContentAggregationId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementId, ColumnMetadata.named("settlementId").withIndex(9).ofType(Types.BIGINT).withSize(20));
        addMetadata(settlementWindowContentId, ColumnMetadata.named("settlementWindowContentId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(transferParticipantRoleTypeId, ColumnMetadata.named("transferParticipantRoleTypeId").withIndex(4).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

