package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLParticipantLimit is a Querydsl query type for CLParticipantLimit
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLParticipantLimit extends com.querydsl.sql.RelationalPathBase<CLParticipantLimit> {

    private static final long serialVersionUID = -1881905275;

    public static final CLParticipantLimit participantLimit = new CLParticipantLimit("participantLimit");

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final NumberPath<Integer> participantCurrencyId = createNumber("participantCurrencyId", Integer.class);

    public final NumberPath<Integer> participantLimitId = createNumber("participantLimitId", Integer.class);

    public final NumberPath<Integer> participantLimitTypeId = createNumber("participantLimitTypeId", Integer.class);

    public final NumberPath<Long> startAfterParticipantPositionChangeId = createNumber("startAfterParticipantPositionChangeId", Long.class);

    public final NumberPath<java.math.BigDecimal> thresholdAlarmPercentage = createNumber("thresholdAlarmPercentage", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> value = createNumber("value", java.math.BigDecimal.class);

    public final com.querydsl.sql.PrimaryKey<CLParticipantLimit> primary = createPrimaryKey(participantLimitId);

    public final com.querydsl.sql.ForeignKey<CLParticipantCurrency> participantlimitParticipantcurrencyidForeign = createForeignKey(participantCurrencyId, "participantCurrencyId");

    public final com.querydsl.sql.ForeignKey<CLParticipantLimitType> participantlimitParticipantlimittypeidForeign = createForeignKey(participantLimitTypeId, "participantLimitTypeId");

    public final com.querydsl.sql.ForeignKey<CLParticipantPositionChange> participantlimitStartafterparticipantpositionchangeidForeign = createForeignKey(startAfterParticipantPositionChangeId, "participantPositionChangeId");

    public CLParticipantLimit(String variable) {
        super(CLParticipantLimit.class, forVariable(variable), "null", "participantLimit");
        addMetadata();
    }

    public CLParticipantLimit(String variable, String schema, String table) {
        super(CLParticipantLimit.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLParticipantLimit(String variable, String schema) {
        super(CLParticipantLimit.class, forVariable(variable), schema, "participantLimit");
        addMetadata();
    }

    public CLParticipantLimit(Path<? extends CLParticipantLimit> path) {
        super(path.getType(), path.getMetadata(), "null", "participantLimit");
        addMetadata();
    }

    public CLParticipantLimit(PathMetadata metadata) {
        super(CLParticipantLimit.class, metadata, "null", "participantLimit");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdBy, ColumnMetadata.named("createdBy").withIndex(9).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(8).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(7).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(participantCurrencyId, ColumnMetadata.named("participantCurrencyId").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(participantLimitId, ColumnMetadata.named("participantLimitId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(participantLimitTypeId, ColumnMetadata.named("participantLimitTypeId").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(startAfterParticipantPositionChangeId, ColumnMetadata.named("startAfterParticipantPositionChangeId").withIndex(6).ofType(Types.BIGINT).withSize(20));
        addMetadata(thresholdAlarmPercentage, ColumnMetadata.named("thresholdAlarmPercentage").withIndex(5).ofType(Types.DECIMAL).withSize(5).withDigits(2).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(4).ofType(Types.DECIMAL).withSize(18).withDigits(4).notNull());
    }

}

