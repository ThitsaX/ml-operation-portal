package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLBulkTransfer is a Querydsl query type for CLBulkTransfer
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLBulkTransfer extends com.querydsl.sql.RelationalPathBase<CLBulkTransfer> {

    private static final long serialVersionUID = 447403226;

    public static final CLBulkTransfer bulkTransfer = new CLBulkTransfer("bulkTransfer");

    public final StringPath bulkQuoteId = createString("bulkQuoteId");

    public final StringPath bulkTransferId = createString("bulkTransferId");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final DateTimePath<java.sql.Timestamp> expirationDate = createDateTime("expirationDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> payeeParticipantId = createNumber("payeeParticipantId", Integer.class);

    public final NumberPath<Integer> payerParticipantId = createNumber("payerParticipantId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLBulkTransfer> primary = createPrimaryKey(bulkTransferId);

    public final com.querydsl.sql.ForeignKey<CLBulkTransferDuplicateCheck> bulktransferBulktransferidForeign = createForeignKey(bulkTransferId, "bulkTransferId");

    public final com.querydsl.sql.ForeignKey<CLParticipant> bulktransferPayeeparticipantidForeign = createForeignKey(payeeParticipantId, "participantId");

    public final com.querydsl.sql.ForeignKey<CLParticipant> bulktransferPayerparticipantidForeign = createForeignKey(payerParticipantId, "participantId");

    public final com.querydsl.sql.ForeignKey<CLBulkTransferAssociation> _bulktransferassociationBulktransferidForeign = createInvForeignKey(bulkTransferId, "bulkTransferId");

    public final com.querydsl.sql.ForeignKey<CLBulkTransferExtension> _bulktransferextensionBulktransferidForeign = createInvForeignKey(bulkTransferId, "bulkTransferId");

    public final com.querydsl.sql.ForeignKey<CLBulkTransferFulfilmentDuplicateCheck> _bulktransferfulfilmentduplicatecheckBulktransferidForeign = createInvForeignKey(bulkTransferId, "bulkTransferId");

    public final com.querydsl.sql.ForeignKey<CLBulkTransferStateChange> _bulktransferstatechangeBulktransferidForeign = createInvForeignKey(bulkTransferId, "bulkTransferId");

    public CLBulkTransfer(String variable) {
        super(CLBulkTransfer.class, forVariable(variable), "null", "bulkTransfer");
        addMetadata();
    }

    public CLBulkTransfer(String variable, String schema, String table) {
        super(CLBulkTransfer.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLBulkTransfer(String variable, String schema) {
        super(CLBulkTransfer.class, forVariable(variable), schema, "bulkTransfer");
        addMetadata();
    }

    public CLBulkTransfer(Path<? extends CLBulkTransfer> path) {
        super(path.getType(), path.getMetadata(), "null", "bulkTransfer");
        addMetadata();
    }

    public CLBulkTransfer(PathMetadata metadata) {
        super(CLBulkTransfer.class, metadata, "null", "bulkTransfer");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(bulkQuoteId, ColumnMetadata.named("bulkQuoteId").withIndex(2).ofType(Types.VARCHAR).withSize(36));
        addMetadata(bulkTransferId, ColumnMetadata.named("bulkTransferId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(6).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(expirationDate, ColumnMetadata.named("expirationDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(payeeParticipantId, ColumnMetadata.named("payeeParticipantId").withIndex(4).ofType(Types.INTEGER).withSize(10));
        addMetadata(payerParticipantId, ColumnMetadata.named("payerParticipantId").withIndex(3).ofType(Types.INTEGER).withSize(10));
    }

}

