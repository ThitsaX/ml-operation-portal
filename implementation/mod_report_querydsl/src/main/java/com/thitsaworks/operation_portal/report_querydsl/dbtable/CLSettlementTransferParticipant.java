package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSettlementTransferParticipant is a Querydsl query type for CLSettlementTransferParticipant
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementTransferParticipant extends com.querydsl.sql.RelationalPathBase<CLSettlementTransferParticipant> {

    private static final long serialVersionUID = 453622338;

    public static final CLSettlementTransferParticipant settlementTransferParticipant = new CLSettlementTransferParticipant("settlementTransferParticipant");

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> ledgerEntryTypeId = createNumber("ledgerEntryTypeId", Integer.class);

    public final NumberPath<Integer> participantCurrencyId = createNumber("participantCurrencyId", Integer.class);

    public final NumberPath<Long> settlementId = createNumber("settlementId", Long.class);

    public final NumberPath<Long> settlementTransferParticipantId = createNumber("settlementTransferParticipantId", Long.class);

    public final NumberPath<Long> settlementWindowId = createNumber("settlementWindowId", Long.class);

    public final NumberPath<Integer> transferParticipantRoleTypeId = createNumber("transferParticipantRoleTypeId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLSettlementTransferParticipant> primary = createPrimaryKey(settlementTransferParticipantId);

    public final com.querydsl.sql.ForeignKey<CLLedgerEntryType> settlementtransferparticipantLedgerentrytypeidForeign = createForeignKey(ledgerEntryTypeId, "ledgerEntryTypeId");

    public final com.querydsl.sql.ForeignKey<CLParticipantCurrency> settlementtransferparticipantParticipantcurrencyidForeign = createForeignKey(participantCurrencyId, "participantCurrencyId");

    public final com.querydsl.sql.ForeignKey<CLSettlement> settlementtransferparticipantSettlementidForeign = createForeignKey(settlementId, "settlementId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindow> settlementtransferparticipantSettlementwindowidForeign = createForeignKey(settlementWindowId, "settlementWindowId");

    public final com.querydsl.sql.ForeignKey<CLTransferParticipantRoleType> stpTransferparticipantroletypeidForeign = createForeignKey(transferParticipantRoleTypeId, "transferParticipantRoleTypeId");

    public CLSettlementTransferParticipant(String variable) {
        super(CLSettlementTransferParticipant.class, forVariable(variable), "null", "settlementTransferParticipant");
        addMetadata();
    }

    public CLSettlementTransferParticipant(String variable, String schema, String table) {
        super(CLSettlementTransferParticipant.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementTransferParticipant(String variable, String schema) {
        super(CLSettlementTransferParticipant.class, forVariable(variable), schema, "settlementTransferParticipant");
        addMetadata();
    }

    public CLSettlementTransferParticipant(Path<? extends CLSettlementTransferParticipant> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementTransferParticipant");
        addMetadata();
    }

    public CLSettlementTransferParticipant(PathMetadata metadata) {
        super(CLSettlementTransferParticipant.class, metadata, "null", "settlementTransferParticipant");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(amount, ColumnMetadata.named("amount").withIndex(7).ofType(Types.DECIMAL).withSize(18).withDigits(4).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(8).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(ledgerEntryTypeId, ColumnMetadata.named("ledgerEntryTypeId").withIndex(6).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(participantCurrencyId, ColumnMetadata.named("participantCurrencyId").withIndex(4).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(settlementId, ColumnMetadata.named("settlementId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementTransferParticipantId, ColumnMetadata.named("settlementTransferParticipantId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementWindowId, ColumnMetadata.named("settlementWindowId").withIndex(3).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(transferParticipantRoleTypeId, ColumnMetadata.named("transferParticipantRoleTypeId").withIndex(5).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

