package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLQuoteParty is a Querydsl query type for CLQuoteParty
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLQuoteParty extends com.querydsl.sql.RelationalPathBase<CLQuoteParty> {

    private static final long serialVersionUID = 1079344839;

    public static final CLQuoteParty quoteParty = new CLQuoteParty("quoteParty");

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath currencyId = createString("currencyId");

    public final StringPath fspId = createString("fspId");

    public final NumberPath<Integer> ledgerEntryTypeId = createNumber("ledgerEntryTypeId", Integer.class);

    public final StringPath merchantClassificationCode = createString("merchantClassificationCode");

    public final NumberPath<Integer> participantId = createNumber("participantId", Integer.class);

    public final NumberPath<Integer> partyIdentifierTypeId = createNumber("partyIdentifierTypeId", Integer.class);

    public final StringPath partyIdentifierValue = createString("partyIdentifierValue");

    public final StringPath partyName = createString("partyName");

    public final StringPath partySubIdOrTypeId = createString("partySubIdOrTypeId");

    public final NumberPath<Integer> partyTypeId = createNumber("partyTypeId", Integer.class);

    public final StringPath quoteId = createString("quoteId");

    public final NumberPath<Long> quotePartyId = createNumber("quotePartyId", Long.class);

    public final NumberPath<Integer> transferParticipantRoleTypeId = createNumber("transferParticipantRoleTypeId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLQuoteParty> primary = createPrimaryKey(quotePartyId);

    public final com.querydsl.sql.ForeignKey<CLCurrency> quotepartyCurrencyidForeign = createForeignKey(currencyId, "currencyId");

    public final com.querydsl.sql.ForeignKey<CLLedgerEntryType> quotepartyLedgerentrytypeidForeign = createForeignKey(ledgerEntryTypeId, "ledgerEntryTypeId");

    public final com.querydsl.sql.ForeignKey<CLParticipant> quotepartyParticipantidForeign = createForeignKey(participantId, "participantId");

    public final com.querydsl.sql.ForeignKey<CLPartyIdentifierType> quotepartyPartyidentifiertypeidForeign = createForeignKey(partyIdentifierTypeId, "partyIdentifierTypeId");

    public final com.querydsl.sql.ForeignKey<CLPartyType> quotepartyPartytypeidForeign = createForeignKey(partyTypeId, "partyTypeId");

    public final com.querydsl.sql.ForeignKey<CLQuote> quotepartyQuoteidForeign = createForeignKey(quoteId, "quoteId");

    public final com.querydsl.sql.ForeignKey<CLTransferParticipantRoleType> quotepartyTransferparticipantroletypeidForeign = createForeignKey(transferParticipantRoleTypeId, "transferParticipantRoleTypeId");

    public final com.querydsl.sql.ForeignKey<CLGeoCode> _geocodeQuotepartyidForeign = createInvForeignKey(quotePartyId, "quotePartyId");

    public final com.querydsl.sql.ForeignKey<CLParty> _partyQuotepartyidForeign = createInvForeignKey(quotePartyId, "quotePartyId");

    public final com.querydsl.sql.ForeignKey<CLQuotePartyIdInfoExtension> _quotepartyidinfoextensionQuotepartyidForeign = createInvForeignKey(quotePartyId, "quotePartyId");

    public CLQuoteParty(String variable) {
        super(CLQuoteParty.class, forVariable(variable), "null", "quoteParty");
        addMetadata();
    }

    public CLQuoteParty(String variable, String schema, String table) {
        super(CLQuoteParty.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLQuoteParty(String variable, String schema) {
        super(CLQuoteParty.class, forVariable(variable), schema, "quoteParty");
        addMetadata();
    }

    public CLQuoteParty(Path<? extends CLQuoteParty> path) {
        super(path.getType(), path.getMetadata(), "null", "quoteParty");
        addMetadata();
    }

    public CLQuoteParty(PathMetadata metadata) {
        super(CLQuoteParty.class, metadata, "null", "quoteParty");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(amount, ColumnMetadata.named("amount").withIndex(13).ofType(Types.DECIMAL).withSize(18).withDigits(4).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(15).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(currencyId, ColumnMetadata.named("currencyId").withIndex(14).ofType(Types.VARCHAR).withSize(3).notNull());
        addMetadata(fspId, ColumnMetadata.named("fspId").withIndex(7).ofType(Types.VARCHAR).withSize(255));
        addMetadata(ledgerEntryTypeId, ColumnMetadata.named("ledgerEntryTypeId").withIndex(12).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(merchantClassificationCode, ColumnMetadata.named("merchantClassificationCode").withIndex(9).ofType(Types.VARCHAR).withSize(4));
        addMetadata(participantId, ColumnMetadata.named("participantId").withIndex(8).ofType(Types.INTEGER).withSize(10));
        addMetadata(partyIdentifierTypeId, ColumnMetadata.named("partyIdentifierTypeId").withIndex(4).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(partyIdentifierValue, ColumnMetadata.named("partyIdentifierValue").withIndex(5).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(partyName, ColumnMetadata.named("partyName").withIndex(10).ofType(Types.VARCHAR).withSize(128));
        addMetadata(partySubIdOrTypeId, ColumnMetadata.named("partySubIdOrTypeId").withIndex(6).ofType(Types.VARCHAR).withSize(128));
        addMetadata(partyTypeId, ColumnMetadata.named("partyTypeId").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(quoteId, ColumnMetadata.named("quoteId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(quotePartyId, ColumnMetadata.named("quotePartyId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(transferParticipantRoleTypeId, ColumnMetadata.named("transferParticipantRoleTypeId").withIndex(11).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

