package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSettlementParticipantCurrency is a Querydsl query type for CLSettlementParticipantCurrency
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementParticipantCurrency extends com.querydsl.sql.RelationalPathBase<CLSettlementParticipantCurrency> {

    private static final long serialVersionUID = 605539422;

    public static final CLSettlementParticipantCurrency settlementParticipantCurrency = new CLSettlementParticipantCurrency("settlementParticipantCurrency");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final NumberPath<Long> currentStateChangeId = createNumber("currentStateChangeId", Long.class);

    public final NumberPath<java.math.BigDecimal> netAmount = createNumber("netAmount", java.math.BigDecimal.class);

    public final NumberPath<Integer> participantCurrencyId = createNumber("participantCurrencyId", Integer.class);

    public final NumberPath<Long> settlementId = createNumber("settlementId", Long.class);

    public final NumberPath<Long> settlementParticipantCurrencyId = createNumber("settlementParticipantCurrencyId", Long.class);

    public final StringPath settlementTransferId = createString("settlementTransferId");

    public final com.querydsl.sql.PrimaryKey<CLSettlementParticipantCurrency> primary = createPrimaryKey(settlementParticipantCurrencyId);

    public final com.querydsl.sql.ForeignKey<CLParticipantCurrency> settlementparticipantcurrencyParticipantcurrencyidForeign = createForeignKey(participantCurrencyId, "participantCurrencyId");

    public final com.querydsl.sql.ForeignKey<CLSettlement> settlementparticipantcurrencySettlementidForeign = createForeignKey(settlementId, "settlementId");

    public final com.querydsl.sql.ForeignKey<CLSettlementParticipantCurrencyStateChange> spcCurrentstatechangeidForeign = createForeignKey(currentStateChangeId, "settlementParticipantCurrencyStateChangeId");

    public final com.querydsl.sql.ForeignKey<CLSettlementParticipantCurrencyStateChange> _spcscSettlementparticipantcurrencyidForeign = createInvForeignKey(settlementParticipantCurrencyId, "settlementParticipantCurrencyId");

    public CLSettlementParticipantCurrency(String variable) {
        super(CLSettlementParticipantCurrency.class, forVariable(variable), "null", "settlementParticipantCurrency");
        addMetadata();
    }

    public CLSettlementParticipantCurrency(String variable, String schema, String table) {
        super(CLSettlementParticipantCurrency.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementParticipantCurrency(String variable, String schema) {
        super(CLSettlementParticipantCurrency.class, forVariable(variable), schema, "settlementParticipantCurrency");
        addMetadata();
    }

    public CLSettlementParticipantCurrency(Path<? extends CLSettlementParticipantCurrency> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementParticipantCurrency");
        addMetadata();
    }

    public CLSettlementParticipantCurrency(PathMetadata metadata) {
        super(CLSettlementParticipantCurrency.class, metadata, "null", "settlementParticipantCurrency");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(currentStateChangeId, ColumnMetadata.named("currentStateChangeId").withIndex(6).ofType(Types.BIGINT).withSize(20));
        addMetadata(netAmount, ColumnMetadata.named("netAmount").withIndex(4).ofType(Types.DECIMAL).withSize(18).withDigits(4).notNull());
        addMetadata(participantCurrencyId, ColumnMetadata.named("participantCurrencyId").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(settlementId, ColumnMetadata.named("settlementId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementParticipantCurrencyId, ColumnMetadata.named("settlementParticipantCurrencyId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementTransferId, ColumnMetadata.named("settlementTransferId").withIndex(7).ofType(Types.VARCHAR).withSize(36));
    }

}

