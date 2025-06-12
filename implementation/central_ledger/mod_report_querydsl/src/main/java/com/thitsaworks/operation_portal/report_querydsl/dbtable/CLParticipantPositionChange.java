package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLParticipantPositionChange is a Querydsl query type for CLParticipantPositionChange
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLParticipantPositionChange extends com.querydsl.sql.RelationalPathBase<CLParticipantPositionChange> {

    private static final long serialVersionUID = -806888017;

    public static final CLParticipantPositionChange participantPositionChange = new CLParticipantPositionChange("participantPositionChange");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final NumberPath<Long> participantPositionChangeId = createNumber("participantPositionChangeId", Long.class);

    public final NumberPath<Long> participantPositionId = createNumber("participantPositionId", Long.class);

    public final NumberPath<java.math.BigDecimal> reservedValue = createNumber("reservedValue", java.math.BigDecimal.class);

    public final NumberPath<Long> transferStateChangeId = createNumber("transferStateChangeId", Long.class);

    public final NumberPath<java.math.BigDecimal> value = createNumber("value", java.math.BigDecimal.class);

    public final com.querydsl.sql.PrimaryKey<CLParticipantPositionChange> primary = createPrimaryKey(participantPositionChangeId);

    public final com.querydsl.sql.ForeignKey<CLParticipantPosition> participantpositionchangeParticipantpositionidForeign = createForeignKey(participantPositionId, "participantPositionId");

    public final com.querydsl.sql.ForeignKey<CLTransferStateChange> participantpositionchangeTransferstatechangeidForeign = createForeignKey(transferStateChangeId, "transferStateChangeId");

    public final com.querydsl.sql.ForeignKey<CLParticipantLimit> _participantlimitStartafterparticipantpositionchangeidForeign = createInvForeignKey(participantPositionChangeId, "startAfterParticipantPositionChangeId");

    public CLParticipantPositionChange(String variable) {
        super(CLParticipantPositionChange.class, forVariable(variable), "null", "participantPositionChange");
        addMetadata();
    }

    public CLParticipantPositionChange(String variable, String schema, String table) {
        super(CLParticipantPositionChange.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLParticipantPositionChange(String variable, String schema) {
        super(CLParticipantPositionChange.class, forVariable(variable), schema, "participantPositionChange");
        addMetadata();
    }

    public CLParticipantPositionChange(Path<? extends CLParticipantPositionChange> path) {
        super(path.getType(), path.getMetadata(), "null", "participantPositionChange");
        addMetadata();
    }

    public CLParticipantPositionChange(PathMetadata metadata) {
        super(CLParticipantPositionChange.class, metadata, "null", "participantPositionChange");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(6).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(participantPositionChangeId, ColumnMetadata.named("participantPositionChangeId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(participantPositionId, ColumnMetadata.named("participantPositionId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(reservedValue, ColumnMetadata.named("reservedValue").withIndex(5).ofType(Types.DECIMAL).withSize(18).withDigits(4).notNull());
        addMetadata(transferStateChangeId, ColumnMetadata.named("transferStateChangeId").withIndex(3).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(4).ofType(Types.DECIMAL).withSize(18).withDigits(4).notNull());
    }

}

