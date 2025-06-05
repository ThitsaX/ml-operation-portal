package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import java.util.*;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLParticipantCurrency is a Querydsl query type for CLParticipantCurrency
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLParticipantCurrency extends com.querydsl.sql.RelationalPathBase<CLParticipantCurrency> {

    private static final long serialVersionUID = 2073872391;

    public static final CLParticipantCurrency participantCurrency = new CLParticipantCurrency("participantCurrency");

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath currencyId = createString("currencyId");

    public final BooleanPath isActive = createBoolean("isActive");

    public final NumberPath<Integer> ledgerAccountTypeId = createNumber("ledgerAccountTypeId", Integer.class);

    public final NumberPath<Integer> participantCurrencyId = createNumber("participantCurrencyId", Integer.class);

    public final NumberPath<Integer> participantId = createNumber("participantId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLParticipantCurrency> primary = createPrimaryKey(participantCurrencyId);

    public final com.querydsl.sql.ForeignKey<CLCurrency> participantcurrencyCurrencyidForeign = createForeignKey(currencyId, "currencyId");

    public final com.querydsl.sql.ForeignKey<CLLedgerAccountType> participantcurrencyLedgeraccounttypeidForeign = createForeignKey(ledgerAccountTypeId, "ledgerAccountTypeId");

    public final com.querydsl.sql.ForeignKey<CLParticipant> participantcurrencyParticipantidForeign = createForeignKey(participantId, "participantId");

    public final com.querydsl.sql.ForeignKey<CLParticipantLimit> _participantlimitParticipantcurrencyidForeign = createInvForeignKey(Arrays.asList(participantCurrencyId, participantCurrencyId), Arrays.asList("participantCurrencyId", "participantCurrencyId"));

    public final com.querydsl.sql.ForeignKey<CLParticipantPosition> _participantpositionParticipantcurrencyidForeign = createInvForeignKey(Arrays.asList(participantCurrencyId, participantCurrencyId), Arrays.asList("participantCurrencyId", "participantCurrencyId"));

    public final com.querydsl.sql.ForeignKey<CLSettlementContentAggregation> _settlementcontentaggregationParticipantcurrencyidForeign = createInvForeignKey(Arrays.asList(participantCurrencyId, participantCurrencyId), Arrays.asList("participantCurrencyId", "participantCurrencyId"));

    public final com.querydsl.sql.ForeignKey<CLSettlementParticipantCurrency> _settlementparticipantcurrencyParticipantcurrencyidForeign = createInvForeignKey(Arrays.asList(participantCurrencyId, participantCurrencyId), Arrays.asList("participantCurrencyId", "participantCurrencyId"));

    public final com.querydsl.sql.ForeignKey<CLSettlementTransferParticipant> _settlementtransferparticipantParticipantcurrencyidForeign = createInvForeignKey(Arrays.asList(participantCurrencyId, participantCurrencyId), Arrays.asList("participantCurrencyId", "participantCurrencyId"));

    public final com.querydsl.sql.ForeignKey<CLTransferParticipant> _transferparticipantParticipantcurrencyidForeign = createInvForeignKey(Arrays.asList(participantCurrencyId, participantCurrencyId), Arrays.asList("participantCurrencyId", "participantCurrencyId"));

    public CLParticipantCurrency(String variable) {
        super(CLParticipantCurrency.class, forVariable(variable), "null", "participantCurrency");
        addMetadata();
    }

    public CLParticipantCurrency(String variable, String schema, String table) {
        super(CLParticipantCurrency.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLParticipantCurrency(String variable, String schema) {
        super(CLParticipantCurrency.class, forVariable(variable), schema, "participantCurrency");
        addMetadata();
    }

    public CLParticipantCurrency(Path<? extends CLParticipantCurrency> path) {
        super(path.getType(), path.getMetadata(), "null", "participantCurrency");
        addMetadata();
    }

    public CLParticipantCurrency(PathMetadata metadata) {
        super(CLParticipantCurrency.class, metadata, "null", "participantCurrency");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdBy, ColumnMetadata.named("createdBy").withIndex(7).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(6).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(currencyId, ColumnMetadata.named("currencyId").withIndex(3).ofType(Types.VARCHAR).withSize(3).notNull());
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(5).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(ledgerAccountTypeId, ColumnMetadata.named("ledgerAccountTypeId").withIndex(4).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(participantCurrencyId, ColumnMetadata.named("participantCurrencyId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(participantId, ColumnMetadata.named("participantId").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

