package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLParty is a Querydsl query type for CLParty
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLParty extends com.querydsl.sql.RelationalPathBase<CLParty> {

    private static final long serialVersionUID = -1714271287;

    public static final CLParty party = new CLParty("party");

    public final DateTimePath<java.sql.Timestamp> dateOfBirth = createDateTime("dateOfBirth", java.sql.Timestamp.class);

    public final StringPath firstName = createString("firstName");

    public final StringPath lastName = createString("lastName");

    public final StringPath middleName = createString("middleName");

    public final NumberPath<Long> partyId = createNumber("partyId", Long.class);

    public final NumberPath<Long> quotePartyId = createNumber("quotePartyId", Long.class);

    public final com.querydsl.sql.PrimaryKey<CLParty> primary = createPrimaryKey(partyId);

    public final com.querydsl.sql.ForeignKey<CLQuoteParty> partyQuotepartyidForeign = createForeignKey(quotePartyId, "quotePartyId");

    public CLParty(String variable) {
        super(CLParty.class, forVariable(variable), "null", "party");
        addMetadata();
    }

    public CLParty(String variable, String schema, String table) {
        super(CLParty.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLParty(String variable, String schema) {
        super(CLParty.class, forVariable(variable), schema, "party");
        addMetadata();
    }

    public CLParty(Path<? extends CLParty> path) {
        super(path.getType(), path.getMetadata(), "null", "party");
        addMetadata();
    }

    public CLParty(PathMetadata metadata) {
        super(CLParty.class, metadata, "null", "party");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(dateOfBirth, ColumnMetadata.named("dateOfBirth").withIndex(6).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(firstName, ColumnMetadata.named("firstName").withIndex(3).ofType(Types.VARCHAR).withSize(128));
        addMetadata(lastName, ColumnMetadata.named("lastName").withIndex(5).ofType(Types.VARCHAR).withSize(128));
        addMetadata(middleName, ColumnMetadata.named("middleName").withIndex(4).ofType(Types.VARCHAR).withSize(128));
        addMetadata(partyId, ColumnMetadata.named("partyId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(quotePartyId, ColumnMetadata.named("quotePartyId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
    }

}

