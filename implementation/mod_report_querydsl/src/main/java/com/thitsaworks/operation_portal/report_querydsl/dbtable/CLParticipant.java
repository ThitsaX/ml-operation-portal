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
 * CLParticipant is a Querydsl query type for CLParticipant
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLParticipant extends com.querydsl.sql.RelationalPathBase<CLParticipant> {

    private static final long serialVersionUID = -788878890;

    public static final CLParticipant participant = new CLParticipant("participant");

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> participantId = createNumber("participantId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLParticipant> primary = createPrimaryKey(participantId);

    public final com.querydsl.sql.ForeignKey<CLBulkTransfer> _bulktransferPayeeparticipantidForeign = createInvForeignKey(Arrays.asList(participantId, participantId), Arrays.asList("payeeParticipantId", "payeeParticipantId"));

    public final com.querydsl.sql.ForeignKey<CLBulkTransfer> _bulktransferPayerparticipantidForeign = createInvForeignKey(Arrays.asList(participantId, participantId), Arrays.asList("payerParticipantId", "payerParticipantId"));

    public final com.querydsl.sql.ForeignKey<CLParticipantContact> _participantcontactParticipantidForeign = createInvForeignKey(Arrays.asList(participantId, participantId), Arrays.asList("participantId", "participantId"));

    public final com.querydsl.sql.ForeignKey<CLParticipantCurrency> _participantcurrencyParticipantidForeign = createInvForeignKey(Arrays.asList(participantId, participantId), Arrays.asList("participantId", "participantId"));

    public final com.querydsl.sql.ForeignKey<CLParticipantEndpoint> _participantendpointParticipantidForeign = createInvForeignKey(Arrays.asList(participantId, participantId), Arrays.asList("participantId", "participantId"));

    public final com.querydsl.sql.ForeignKey<CLParticipantParty> _participantpartyParticipantidForeign = createInvForeignKey(Arrays.asList(participantId, participantId), Arrays.asList("participantId", "participantId"));

    public final com.querydsl.sql.ForeignKey<CLQuoteParty> _quotepartyParticipantidForeign = createInvForeignKey(Arrays.asList(participantId, participantId), Arrays.asList("participantId", "participantId"));

    public final com.querydsl.sql.ForeignKey<CLToken> _tokenParticipantidForeign = createInvForeignKey(Arrays.asList(participantId, participantId), Arrays.asList("participantId", "participantId"));

    public CLParticipant(String variable) {
        super(CLParticipant.class, forVariable(variable), "null", "participant");
        addMetadata();
    }

    public CLParticipant(String variable, String schema, String table) {
        super(CLParticipant.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLParticipant(String variable, String schema) {
        super(CLParticipant.class, forVariable(variable), schema, "participant");
        addMetadata();
    }

    public CLParticipant(Path<? extends CLParticipant> path) {
        super(path.getType(), path.getMetadata(), "null", "participant");
        addMetadata();
    }

    public CLParticipant(PathMetadata metadata) {
        super(CLParticipant.class, metadata, "null", "participant");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdBy, ColumnMetadata.named("createdBy").withIndex(6).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(256).notNull());
        addMetadata(participantId, ColumnMetadata.named("participantId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

