package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLQuote is a Querydsl query type for CLQuote
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLQuote extends com.querydsl.sql.RelationalPathBase<CLQuote> {

    private static final long serialVersionUID = -1712754849;

    public static final CLQuote quote = new CLQuote("quote");

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final NumberPath<Integer> amountTypeId = createNumber("amountTypeId", Integer.class);

    public final NumberPath<Integer> balanceOfPaymentsId = createNumber("balanceOfPaymentsId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath currencyId = createString("currencyId");

    public final DateTimePath<java.sql.Timestamp> expirationDate = createDateTime("expirationDate", java.sql.Timestamp.class);

    public final StringPath note = createString("note");

    public final StringPath quoteId = createString("quoteId");

    public final NumberPath<Integer> transactionInitiatorId = createNumber("transactionInitiatorId", Integer.class);

    public final NumberPath<Integer> transactionInitiatorTypeId = createNumber("transactionInitiatorTypeId", Integer.class);

    public final StringPath transactionReferenceId = createString("transactionReferenceId");

    public final StringPath transactionRequestId = createString("transactionRequestId");

    public final NumberPath<Integer> transactionScenarioId = createNumber("transactionScenarioId", Integer.class);

    public final NumberPath<Integer> transactionSubScenarioId = createNumber("transactionSubScenarioId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLQuote> primary = createPrimaryKey(quoteId);

    public final com.querydsl.sql.ForeignKey<CLAmountType> quoteAmounttypeidForeign = createForeignKey(amountTypeId, "amountTypeId");

    public final com.querydsl.sql.ForeignKey<CLBalanceOfPayments> quoteBalanceofpaymentsidForeign = createForeignKey(balanceOfPaymentsId, "balanceOfPaymentsId");

    public final com.querydsl.sql.ForeignKey<CLCurrency> quoteCurrencyidForeign = createForeignKey(currencyId, "currencyId");

    public final com.querydsl.sql.ForeignKey<CLTransactionInitiator> quoteTransactioninitiatoridForeign = createForeignKey(transactionInitiatorId, "transactionInitiatorId");

    public final com.querydsl.sql.ForeignKey<CLTransactionInitiatorType> quoteTransactioninitiatortypeidForeign = createForeignKey(transactionInitiatorTypeId, "transactionInitiatorTypeId");

    public final com.querydsl.sql.ForeignKey<CLTransactionReference> quoteTransactionreferenceidForeign = createForeignKey(transactionReferenceId, "transactionReferenceId");

    public final com.querydsl.sql.ForeignKey<CLTransactionScenario> quoteTransactionscenarioidForeign = createForeignKey(transactionScenarioId, "transactionScenarioId");

    public final com.querydsl.sql.ForeignKey<CLTransactionSubScenario> quoteTransactionsubscenarioidForeign = createForeignKey(transactionSubScenarioId, "transactionSubScenarioId");

    public final com.querydsl.sql.ForeignKey<CLQuoteError> _quoteerrorQuoteidForeign = createInvForeignKey(quoteId, "quoteId");

    public final com.querydsl.sql.ForeignKey<CLQuoteExtension> _quoteextensionQuoteidForeign = createInvForeignKey(quoteId, "quoteId");

    public final com.querydsl.sql.ForeignKey<CLQuoteParty> _quotepartyQuoteidForeign = createInvForeignKey(quoteId, "quoteId");

    public final com.querydsl.sql.ForeignKey<CLQuoteResponse> _quoteresponseQuoteidForeign = createInvForeignKey(quoteId, "quoteId");

    public final com.querydsl.sql.ForeignKey<CLQuoteResponseDuplicateCheck> _quoteresponseduplicatecheckQuoteidForeign = createInvForeignKey(quoteId, "quoteId");

    public CLQuote(String variable) {
        super(CLQuote.class, forVariable(variable), "null", "quote");
        addMetadata();
    }

    public CLQuote(String variable, String schema, String table) {
        super(CLQuote.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLQuote(String variable, String schema) {
        super(CLQuote.class, forVariable(variable), schema, "quote");
        addMetadata();
    }

    public CLQuote(Path<? extends CLQuote> path) {
        super(path.getType(), path.getMetadata(), "null", "quote");
        addMetadata();
    }

    public CLQuote(PathMetadata metadata) {
        super(CLQuote.class, metadata, "null", "quote");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(amount, ColumnMetadata.named("amount").withIndex(12).ofType(Types.DECIMAL).withSize(18).withDigits(4).notNull());
        addMetadata(amountTypeId, ColumnMetadata.named("amountTypeId").withIndex(11).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(balanceOfPaymentsId, ColumnMetadata.named("balanceOfPaymentsId").withIndex(9).ofType(Types.INTEGER).withSize(10));
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(14).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(currencyId, ColumnMetadata.named("currencyId").withIndex(13).ofType(Types.VARCHAR).withSize(255));
        addMetadata(expirationDate, ColumnMetadata.named("expirationDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(note, ColumnMetadata.named("note").withIndex(4).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(quoteId, ColumnMetadata.named("quoteId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(transactionInitiatorId, ColumnMetadata.named("transactionInitiatorId").withIndex(6).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(transactionInitiatorTypeId, ColumnMetadata.named("transactionInitiatorTypeId").withIndex(7).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(transactionReferenceId, ColumnMetadata.named("transactionReferenceId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(transactionRequestId, ColumnMetadata.named("transactionRequestId").withIndex(3).ofType(Types.VARCHAR).withSize(36));
        addMetadata(transactionScenarioId, ColumnMetadata.named("transactionScenarioId").withIndex(8).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(transactionSubScenarioId, ColumnMetadata.named("transactionSubScenarioId").withIndex(10).ofType(Types.INTEGER).withSize(10));
    }

}

