package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLParticipantParty is a Querydsl query type for CLParticipantParty
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLParticipantParty extends com.querydsl.sql.RelationalPathBase<CLParticipantParty> {

    private static final long serialVersionUID = -1878444368;

    public static final CLParticipantParty participantParty = new CLParticipantParty("participantParty");

    public final NumberPath<Integer> participantId = createNumber("participantId", Integer.class);

    public final NumberPath<Long> participantPartyId = createNumber("participantPartyId", Long.class);

    public final NumberPath<Long> partyId = createNumber("partyId", Long.class);

    public final com.querydsl.sql.PrimaryKey<CLParticipantParty> primary = createPrimaryKey(participantPartyId);

    public final com.querydsl.sql.ForeignKey<CLParticipant> participantpartyParticipantidForeign = createForeignKey(participantId, "participantId");

    public CLParticipantParty(String variable) {
        super(CLParticipantParty.class, forVariable(variable), "null", "participantParty");
        addMetadata();
    }

    public CLParticipantParty(String variable, String schema, String table) {
        super(CLParticipantParty.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLParticipantParty(String variable, String schema) {
        super(CLParticipantParty.class, forVariable(variable), schema, "participantParty");
        addMetadata();
    }

    public CLParticipantParty(Path<? extends CLParticipantParty> path) {
        super(path.getType(), path.getMetadata(), "null", "participantParty");
        addMetadata();
    }

    public CLParticipantParty(PathMetadata metadata) {
        super(CLParticipantParty.class, metadata, "null", "participantParty");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(participantId, ColumnMetadata.named("participantId").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(participantPartyId, ColumnMetadata.named("participantPartyId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(partyId, ColumnMetadata.named("partyId").withIndex(3).ofType(Types.BIGINT).withSize(20).notNull());
    }

}

