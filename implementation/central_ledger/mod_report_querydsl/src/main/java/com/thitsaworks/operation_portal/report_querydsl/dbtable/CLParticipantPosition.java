package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLParticipantPosition is a Querydsl query type for CLParticipantPosition
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLParticipantPosition extends com.querydsl.sql.RelationalPathBase<CLParticipantPosition> {

    private static final long serialVersionUID = -2048691937;

    public static final CLParticipantPosition participantPosition = new CLParticipantPosition("participantPosition");

    public final DateTimePath<java.sql.Timestamp> changedDate = createDateTime("changedDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> participantCurrencyId = createNumber("participantCurrencyId", Integer.class);

    public final NumberPath<Long> participantPositionId = createNumber("participantPositionId", Long.class);

    public final NumberPath<java.math.BigDecimal> reservedValue = createNumber("reservedValue", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> value = createNumber("value", java.math.BigDecimal.class);

    public final com.querydsl.sql.PrimaryKey<CLParticipantPosition> primary = createPrimaryKey(participantPositionId);

    public final com.querydsl.sql.ForeignKey<CLParticipantCurrency> participantpositionParticipantcurrencyidForeign = createForeignKey(participantCurrencyId, "participantCurrencyId");

    public final com.querydsl.sql.ForeignKey<CLParticipantPositionChange> _participantpositionchangeParticipantpositionidForeign = createInvForeignKey(participantPositionId, "participantPositionId");

    public CLParticipantPosition(String variable) {
        super(CLParticipantPosition.class, forVariable(variable), "null", "participantPosition");
        addMetadata();
    }

    public CLParticipantPosition(String variable, String schema, String table) {
        super(CLParticipantPosition.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLParticipantPosition(String variable, String schema) {
        super(CLParticipantPosition.class, forVariable(variable), schema, "participantPosition");
        addMetadata();
    }

    public CLParticipantPosition(Path<? extends CLParticipantPosition> path) {
        super(path.getType(), path.getMetadata(), "null", "participantPosition");
        addMetadata();
    }

    public CLParticipantPosition(PathMetadata metadata) {
        super(CLParticipantPosition.class, metadata, "null", "participantPosition");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(changedDate, ColumnMetadata.named("changedDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(participantCurrencyId, ColumnMetadata.named("participantCurrencyId").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(participantPositionId, ColumnMetadata.named("participantPositionId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(reservedValue, ColumnMetadata.named("reservedValue").withIndex(4).ofType(Types.DECIMAL).withSize(18).withDigits(4).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(3).ofType(Types.DECIMAL).withSize(18).withDigits(4).notNull());
    }

}

