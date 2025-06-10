package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransferParticipant is a Querydsl query type for CLTransferParticipant
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransferParticipant extends com.querydsl.sql.RelationalPathBase<CLTransferParticipant> {

    private static final long serialVersionUID = 1921955307;

    public static final CLTransferParticipant transferParticipant = new CLTransferParticipant("transferParticipant");

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> ledgerEntryTypeId = createNumber("ledgerEntryTypeId", Integer.class);

    public final NumberPath<Integer> participantCurrencyId = createNumber("participantCurrencyId", Integer.class);

    public final StringPath transferId = createString("transferId");

    public final NumberPath<Long> transferParticipantId = createNumber("transferParticipantId", Long.class);

    public final NumberPath<Integer> transferParticipantRoleTypeId = createNumber("transferParticipantRoleTypeId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLTransferParticipant> primary = createPrimaryKey(transferParticipantId);

    public final com.querydsl.sql.ForeignKey<CLLedgerEntryType> transferparticipantLedgerentrytypeidForeign = createForeignKey(ledgerEntryTypeId, "ledgerEntryTypeId");

    public final com.querydsl.sql.ForeignKey<CLParticipantCurrency> transferparticipantParticipantcurrencyidForeign = createForeignKey(participantCurrencyId, "participantCurrencyId");

    public final com.querydsl.sql.ForeignKey<CLTransfer> transferparticipantTransferidForeign = createForeignKey(transferId, "transferId");

    public final com.querydsl.sql.ForeignKey<CLTransferParticipantRoleType> transferparticipantTransferparticipantroletypeidForeign = createForeignKey(transferParticipantRoleTypeId, "transferParticipantRoleTypeId");

    public CLTransferParticipant(String variable) {
        super(CLTransferParticipant.class, forVariable(variable), "null", "transferParticipant");
        addMetadata();
    }

    public CLTransferParticipant(String variable, String schema, String table) {
        super(CLTransferParticipant.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransferParticipant(String variable, String schema) {
        super(CLTransferParticipant.class, forVariable(variable), schema, "transferParticipant");
        addMetadata();
    }

    public CLTransferParticipant(Path<? extends CLTransferParticipant> path) {
        super(path.getType(), path.getMetadata(), "null", "transferParticipant");
        addMetadata();
    }

    public CLTransferParticipant(PathMetadata metadata) {
        super(CLTransferParticipant.class, metadata, "null", "transferParticipant");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(amount, ColumnMetadata.named("amount").withIndex(6).ofType(Types.DECIMAL).withSize(18).withDigits(4).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(7).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(ledgerEntryTypeId, ColumnMetadata.named("ledgerEntryTypeId").withIndex(5).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(participantCurrencyId, ColumnMetadata.named("participantCurrencyId").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(transferId, ColumnMetadata.named("transferId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(transferParticipantId, ColumnMetadata.named("transferParticipantId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(transferParticipantRoleTypeId, ColumnMetadata.named("transferParticipantRoleTypeId").withIndex(4).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

