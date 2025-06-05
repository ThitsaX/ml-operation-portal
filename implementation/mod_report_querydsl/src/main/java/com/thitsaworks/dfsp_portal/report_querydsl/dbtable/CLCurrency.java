package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLCurrency is a Querydsl query type for CLCurrency
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLCurrency extends com.querydsl.sql.RelationalPathBase<CLCurrency> {

    private static final long serialVersionUID = 986378318;

    public static final CLCurrency currency = new CLCurrency("currency");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath currencyId = createString("currencyId");

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> scale = createNumber("scale", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLCurrency> primary = createPrimaryKey(currencyId);

    public final com.querydsl.sql.ForeignKey<CLParticipantCurrency> _participantcurrencyCurrencyidForeign = createInvForeignKey(currencyId, "currencyId");

    public final com.querydsl.sql.ForeignKey<CLQuote> _quoteCurrencyidForeign = createInvForeignKey(currencyId, "currencyId");

    public final com.querydsl.sql.ForeignKey<CLQuoteParty> _quotepartyCurrencyidForeign = createInvForeignKey(currencyId, "currencyId");

    public final com.querydsl.sql.ForeignKey<CLQuoteResponse> _quoteresponsePayeefspcommissioncurrencyidForeign = createInvForeignKey(currencyId, "payeeFspCommissionCurrencyId");

    public final com.querydsl.sql.ForeignKey<CLQuoteResponse> _quoteresponsePayeereceiveamountcurrencyidForeign = createInvForeignKey(currencyId, "payeeReceiveAmountCurrencyId");

    public final com.querydsl.sql.ForeignKey<CLQuoteResponse> _quoteresponseTransferamountcurrencyidForeign = createInvForeignKey(currencyId, "transferAmountCurrencyId");

    public final com.querydsl.sql.ForeignKey<CLSettlementModel> _settlementmodelCurrencyidForeign = createInvForeignKey(currencyId, "currencyId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowContent> _settlementwindowcontentCurrencyidForeign = createInvForeignKey(currencyId, "currencyId");

    public final com.querydsl.sql.ForeignKey<CLTransfer> _transferCurrencyidForeign = createInvForeignKey(currencyId, "currencyId");

    public CLCurrency(String variable) {
        super(CLCurrency.class, forVariable(variable), "null", "currency");
        addMetadata();
    }

    public CLCurrency(String variable, String schema, String table) {
        super(CLCurrency.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLCurrency(String variable, String schema) {
        super(CLCurrency.class, forVariable(variable), schema, "currency");
        addMetadata();
    }

    public CLCurrency(Path<? extends CLCurrency> path) {
        super(path.getType(), path.getMetadata(), "null", "currency");
        addMetadata();
    }

    public CLCurrency(PathMetadata metadata) {
        super(CLCurrency.class, metadata, "null", "currency");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(currencyId, ColumnMetadata.named("currencyId").withIndex(1).ofType(Types.VARCHAR).withSize(3).notNull());
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(3).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(128));
        addMetadata(scale, ColumnMetadata.named("scale").withIndex(5).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

