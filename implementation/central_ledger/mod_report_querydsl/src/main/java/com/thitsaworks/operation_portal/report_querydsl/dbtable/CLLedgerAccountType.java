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
 * CLLedgerAccountType is a Querydsl query type for CLLedgerAccountType
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLLedgerAccountType extends com.querydsl.sql.RelationalPathBase<CLLedgerAccountType> {

    private static final long serialVersionUID = 1507873345;

    public static final CLLedgerAccountType ledgerAccountType = new CLLedgerAccountType("ledgerAccountType");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final BooleanPath isActive = createBoolean("isActive");

    public final BooleanPath isSettleable = createBoolean("isSettleable");

    public final NumberPath<Integer> ledgerAccountTypeId = createNumber("ledgerAccountTypeId", Integer.class);

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<CLLedgerAccountType> primary = createPrimaryKey(ledgerAccountTypeId);

    public final com.querydsl.sql.ForeignKey<CLLedgerEntryType> _ledgerentrytypeLedgeraccounttypeidForeign = createInvForeignKey(Arrays.asList(ledgerAccountTypeId, ledgerAccountTypeId), Arrays.asList("ledgerAccountTypeId", "ledgerAccountTypeId"));

    public final com.querydsl.sql.ForeignKey<CLParticipantCurrency> _participantcurrencyLedgeraccounttypeidForeign = createInvForeignKey(Arrays.asList(ledgerAccountTypeId, ledgerAccountTypeId), Arrays.asList("ledgerAccountTypeId", "ledgerAccountTypeId"));

    public final com.querydsl.sql.ForeignKey<CLSettlementModel> _settlementmodelLedgeraccounttypeidForeign = createInvForeignKey(Arrays.asList(ledgerAccountTypeId, ledgerAccountTypeId), Arrays.asList("ledgerAccountTypeId", "ledgerAccountTypeId"));

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowContent> _settlementwindowcontentLedgeraccounttypeidForeign = createInvForeignKey(Arrays.asList(ledgerAccountTypeId, ledgerAccountTypeId), Arrays.asList("ledgerAccountTypeId", "ledgerAccountTypeId"));

    public CLLedgerAccountType(String variable) {
        super(CLLedgerAccountType.class, forVariable(variable), "null", "ledgerAccountType");
        addMetadata();
    }

    public CLLedgerAccountType(String variable, String schema, String table) {
        super(CLLedgerAccountType.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLLedgerAccountType(String variable, String schema) {
        super(CLLedgerAccountType.class, forVariable(variable), schema, "ledgerAccountType");
        addMetadata();
    }

    public CLLedgerAccountType(Path<? extends CLLedgerAccountType> path) {
        super(path.getType(), path.getMetadata(), "null", "ledgerAccountType");
        addMetadata();
    }

    public CLLedgerAccountType(PathMetadata metadata) {
        super(CLLedgerAccountType.class, metadata, "null", "ledgerAccountType");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(isSettleable, ColumnMetadata.named("isSettleable").withIndex(6).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(ledgerAccountTypeId, ColumnMetadata.named("ledgerAccountTypeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
    }

}

