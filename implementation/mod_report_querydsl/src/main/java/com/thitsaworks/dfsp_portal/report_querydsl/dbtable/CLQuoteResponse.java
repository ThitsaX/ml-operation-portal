package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLQuoteResponse is a Querydsl query type for CLQuoteResponse
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLQuoteResponse extends com.querydsl.sql.RelationalPathBase<CLQuoteResponse> {

    private static final long serialVersionUID = 1107368128;

    public static final CLQuoteResponse quoteResponse = new CLQuoteResponse("quoteResponse");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath ilpCondition = createString("ilpCondition");

    public final BooleanPath isValid = createBoolean("isValid");

    public final NumberPath<java.math.BigDecimal> payeeFspCommissionAmount = createNumber("payeeFspCommissionAmount", java.math.BigDecimal.class);

    public final StringPath payeeFspCommissionCurrencyId = createString("payeeFspCommissionCurrencyId");

    public final NumberPath<java.math.BigDecimal> payeeFspFeeAmount = createNumber("payeeFspFeeAmount", java.math.BigDecimal.class);

    public final StringPath payeeFspFeeCurrencyId = createString("payeeFspFeeCurrencyId");

    public final NumberPath<java.math.BigDecimal> payeeReceiveAmount = createNumber("payeeReceiveAmount", java.math.BigDecimal.class);

    public final StringPath payeeReceiveAmountCurrencyId = createString("payeeReceiveAmountCurrencyId");

    public final StringPath quoteId = createString("quoteId");

    public final NumberPath<Long> quoteResponseId = createNumber("quoteResponseId", Long.class);

    public final DateTimePath<java.sql.Timestamp> responseExpirationDate = createDateTime("responseExpirationDate", java.sql.Timestamp.class);

    public final NumberPath<java.math.BigDecimal> transferAmount = createNumber("transferAmount", java.math.BigDecimal.class);

    public final StringPath transferAmountCurrencyId = createString("transferAmountCurrencyId");

    public final com.querydsl.sql.PrimaryKey<CLQuoteResponse> primary = createPrimaryKey(quoteResponseId);

    public final com.querydsl.sql.ForeignKey<CLCurrency> quoteresponsePayeefspcommissioncurrencyidForeign = createForeignKey(payeeFspCommissionCurrencyId, "currencyId");

    public final com.querydsl.sql.ForeignKey<CLCurrency> quoteresponsePayeereceiveamountcurrencyidForeign = createForeignKey(payeeReceiveAmountCurrencyId, "currencyId");

    public final com.querydsl.sql.ForeignKey<CLQuote> quoteresponseQuoteidForeign = createForeignKey(quoteId, "quoteId");

    public final com.querydsl.sql.ForeignKey<CLCurrency> quoteresponseTransferamountcurrencyidForeign = createForeignKey(transferAmountCurrencyId, "currencyId");

    public final com.querydsl.sql.ForeignKey<CLQuoteError> _quoteerrorQuoteresponseidForeign = createInvForeignKey(quoteResponseId, "quoteResponseId");

    public final com.querydsl.sql.ForeignKey<CLQuoteExtension> _quoteextensionQuoteresponseidForeign = createInvForeignKey(quoteResponseId, "quoteResponseId");

    public final com.querydsl.sql.ForeignKey<CLQuoteResponseDuplicateCheck> _quoteresponseduplicatecheckQuoteresponseidForeign = createInvForeignKey(quoteResponseId, "quoteResponseId");

    public final com.querydsl.sql.ForeignKey<CLQuoteResponseIlpPacket> _quoteresponseilppacketQuoteresponseidForeign = createInvForeignKey(quoteResponseId, "quoteResponseId");

    public CLQuoteResponse(String variable) {
        super(CLQuoteResponse.class, forVariable(variable), "null", "quoteResponse");
        addMetadata();
    }

    public CLQuoteResponse(String variable, String schema, String table) {
        super(CLQuoteResponse.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLQuoteResponse(String variable, String schema) {
        super(CLQuoteResponse.class, forVariable(variable), schema, "quoteResponse");
        addMetadata();
    }

    public CLQuoteResponse(Path<? extends CLQuoteResponse> path) {
        super(path.getType(), path.getMetadata(), "null", "quoteResponse");
        addMetadata();
    }

    public CLQuoteResponse(PathMetadata metadata) {
        super(CLQuoteResponse.class, metadata, "null", "quoteResponse");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(14).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(ilpCondition, ColumnMetadata.named("ilpCondition").withIndex(11).ofType(Types.VARCHAR).withSize(256).notNull());
        addMetadata(isValid, ColumnMetadata.named("isValid").withIndex(13).ofType(Types.BIT).withSize(1));
        addMetadata(payeeFspCommissionAmount, ColumnMetadata.named("payeeFspCommissionAmount").withIndex(10).ofType(Types.DECIMAL).withSize(18).withDigits(4));
        addMetadata(payeeFspCommissionCurrencyId, ColumnMetadata.named("payeeFspCommissionCurrencyId").withIndex(9).ofType(Types.VARCHAR).withSize(3));
        addMetadata(payeeFspFeeAmount, ColumnMetadata.named("payeeFspFeeAmount").withIndex(8).ofType(Types.DECIMAL).withSize(18).withDigits(4));
        addMetadata(payeeFspFeeCurrencyId, ColumnMetadata.named("payeeFspFeeCurrencyId").withIndex(7).ofType(Types.VARCHAR).withSize(3));
        addMetadata(payeeReceiveAmount, ColumnMetadata.named("payeeReceiveAmount").withIndex(6).ofType(Types.DECIMAL).withSize(18).withDigits(4));
        addMetadata(payeeReceiveAmountCurrencyId, ColumnMetadata.named("payeeReceiveAmountCurrencyId").withIndex(5).ofType(Types.VARCHAR).withSize(3));
        addMetadata(quoteId, ColumnMetadata.named("quoteId").withIndex(2).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(quoteResponseId, ColumnMetadata.named("quoteResponseId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(responseExpirationDate, ColumnMetadata.named("responseExpirationDate").withIndex(12).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(transferAmount, ColumnMetadata.named("transferAmount").withIndex(4).ofType(Types.DECIMAL).withSize(18).withDigits(4).notNull());
        addMetadata(transferAmountCurrencyId, ColumnMetadata.named("transferAmountCurrencyId").withIndex(3).ofType(Types.VARCHAR).withSize(3).notNull());
    }

}

