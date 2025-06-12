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
 * CLLedgerEntryType is a Querydsl query type for CLLedgerEntryType
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLLedgerEntryType extends com.querydsl.sql.RelationalPathBase<CLLedgerEntryType> {

    private static final long serialVersionUID = 365997190;

    public static final CLLedgerEntryType ledgerEntryType = new CLLedgerEntryType("ledgerEntryType");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final BooleanPath isActive = createBoolean("isActive");

    public final NumberPath<Integer> ledgerAccountTypeId = createNumber("ledgerAccountTypeId", Integer.class);

    public final NumberPath<Integer> ledgerEntryTypeId = createNumber("ledgerEntryTypeId", Integer.class);

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<CLLedgerEntryType> primary = createPrimaryKey(ledgerEntryTypeId);

    public final com.querydsl.sql.ForeignKey<CLLedgerAccountType> ledgerentrytypeLedgeraccounttypeidForeign = createForeignKey(ledgerAccountTypeId, "ledgerAccountTypeId");

    public final com.querydsl.sql.ForeignKey<CLQuoteParty> _quotepartyLedgerentrytypeidForeign = createInvForeignKey(Arrays.asList(ledgerEntryTypeId, ledgerEntryTypeId), Arrays.asList("ledgerEntryTypeId", "ledgerEntryTypeId"));

    public final com.querydsl.sql.ForeignKey<CLSettlementContentAggregation> _settlementcontentaggregationLedgerentrytypeidForeign = createInvForeignKey(Arrays.asList(ledgerEntryTypeId, ledgerEntryTypeId), Arrays.asList("ledgerEntryTypeId", "ledgerEntryTypeId"));

    public final com.querydsl.sql.ForeignKey<CLSettlementTransferParticipant> _settlementtransferparticipantLedgerentrytypeidForeign = createInvForeignKey(Arrays.asList(ledgerEntryTypeId, ledgerEntryTypeId), Arrays.asList("ledgerEntryTypeId", "ledgerEntryTypeId"));

    public final com.querydsl.sql.ForeignKey<CLTransferParticipant> _transferparticipantLedgerentrytypeidForeign = createInvForeignKey(Arrays.asList(ledgerEntryTypeId, ledgerEntryTypeId), Arrays.asList("ledgerEntryTypeId", "ledgerEntryTypeId"));

    public CLLedgerEntryType(String variable) {
        super(CLLedgerEntryType.class, forVariable(variable), "null", "ledgerEntryType");
        addMetadata();
    }

    public CLLedgerEntryType(String variable, String schema, String table) {
        super(CLLedgerEntryType.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLLedgerEntryType(String variable, String schema) {
        super(CLLedgerEntryType.class, forVariable(variable), schema, "ledgerEntryType");
        addMetadata();
    }

    public CLLedgerEntryType(Path<? extends CLLedgerEntryType> path) {
        super(path.getType(), path.getMetadata(), "null", "ledgerEntryType");
        addMetadata();
    }

    public CLLedgerEntryType(PathMetadata metadata) {
        super(CLLedgerEntryType.class, metadata, "null", "ledgerEntryType");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(ledgerAccountTypeId, ColumnMetadata.named("ledgerAccountTypeId").withIndex(6).ofType(Types.INTEGER).withSize(10));
        addMetadata(ledgerEntryTypeId, ColumnMetadata.named("ledgerEntryTypeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
    }

}

