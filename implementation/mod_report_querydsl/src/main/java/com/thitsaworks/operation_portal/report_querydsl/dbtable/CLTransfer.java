package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransfer is a Querydsl query type for CLTransfer
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransfer extends com.querydsl.sql.RelationalPathBase<CLTransfer> {

    private static final long serialVersionUID = 1691858984;

    public static final CLTransfer transfer = new CLTransfer("transfer");

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath currencyId = createString("currencyId");

    public final DateTimePath<java.sql.Timestamp> expirationDate = createDateTime("expirationDate", java.sql.Timestamp.class);

    public final StringPath ilpCondition = createString("ilpCondition");

    public final StringPath transferId = createString("transferId");

    public final com.querydsl.sql.PrimaryKey<CLTransfer> primary = createPrimaryKey(transferId);

    public final com.querydsl.sql.ForeignKey<CLCurrency> transferCurrencyidForeign = createForeignKey(currencyId, "currencyId");

    public final com.querydsl.sql.ForeignKey<CLTransferDuplicateCheck> transferTransferidForeign = createForeignKey(transferId, "transferId");

    public final com.querydsl.sql.ForeignKey<CLExpiringTransfer> _expiringtransferTransferidForeign = createInvForeignKey(transferId, "transferId");

    public final com.querydsl.sql.ForeignKey<CLIlpPacket> _ilppacketTransferidForeign = createInvForeignKey(transferId, "transferId");

    public final com.querydsl.sql.ForeignKey<CLTransferErrorDuplicateCheck> _transfererrorduplicatecheckTransferidForeign = createInvForeignKey(transferId, "transferId");

    public final com.querydsl.sql.ForeignKey<CLTransferExtension> _transferextensionTransferidForeign = createInvForeignKey(transferId, "transferId");

    public final com.querydsl.sql.ForeignKey<CLTransferFulfilmentDuplicateCheck> _transferfulfilmentduplicatecheckTransferidForeign = createInvForeignKey(transferId, "transferId");

    public final com.querydsl.sql.ForeignKey<CLTransferParticipant> _transferparticipantTransferidForeign = createInvForeignKey(transferId, "transferId");

    public final com.querydsl.sql.ForeignKey<CLTransferStateChange> _transferstatechangeTransferidForeign = createInvForeignKey(transferId, "transferId");

    public final com.querydsl.sql.ForeignKey<CLTransferTimeout> _transfertimeoutTransferidForeign = createInvForeignKey(transferId, "transferId");

    public CLTransfer(String variable) {
        super(CLTransfer.class, forVariable(variable), "null", "transfer");
        addMetadata();
    }

    public CLTransfer(String variable, String schema, String table) {
        super(CLTransfer.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransfer(String variable, String schema) {
        super(CLTransfer.class, forVariable(variable), schema, "transfer");
        addMetadata();
    }

    public CLTransfer(Path<? extends CLTransfer> path) {
        super(path.getType(), path.getMetadata(), "null", "transfer");
        addMetadata();
    }

    public CLTransfer(PathMetadata metadata) {
        super(CLTransfer.class, metadata, "null", "transfer");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(amount, ColumnMetadata.named("amount").withIndex(2).ofType(Types.DECIMAL).withSize(18).withDigits(4).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(6).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(currencyId, ColumnMetadata.named("currencyId").withIndex(3).ofType(Types.VARCHAR).withSize(3).notNull());
        addMetadata(expirationDate, ColumnMetadata.named("expirationDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(ilpCondition, ColumnMetadata.named("ilpCondition").withIndex(4).ofType(Types.VARCHAR).withSize(256).notNull());
        addMetadata(transferId, ColumnMetadata.named("transferId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
    }

}

