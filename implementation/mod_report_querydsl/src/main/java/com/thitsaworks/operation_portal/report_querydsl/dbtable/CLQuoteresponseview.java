package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLQuoteresponseview is a Querydsl query type for CLQuoteresponseview
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLQuoteresponseview extends com.querydsl.sql.RelationalPathBase<CLQuoteresponseview> {

    private static final long serialVersionUID = -1397579963;

    public static final CLQuoteresponseview quoteresponseview = new CLQuoteresponseview("quoteresponseview");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath ilpCondition = createString("ilpCondition");

    public final StringPath ilpPacket = createString("ilpPacket");

    public final BooleanPath isValid = createBoolean("isValid");

    public final StringPath latitude = createString("latitude");

    public final StringPath longitude = createString("longitude");

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

    public CLQuoteresponseview(String variable) {
        super(CLQuoteresponseview.class, forVariable(variable), "null", "quoteresponseview");
        addMetadata();
    }

    public CLQuoteresponseview(String variable, String schema, String table) {
        super(CLQuoteresponseview.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLQuoteresponseview(String variable, String schema) {
        super(CLQuoteresponseview.class, forVariable(variable), schema, "quoteresponseview");
        addMetadata();
    }

    public CLQuoteresponseview(Path<? extends CLQuoteresponseview> path) {
        super(path.getType(), path.getMetadata(), "null", "quoteresponseview");
        addMetadata();
    }

    public CLQuoteresponseview(PathMetadata metadata) {
        super(CLQuoteresponseview.class, metadata, "null", "quoteresponseview");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(14).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(ilpCondition, ColumnMetadata.named("ilpCondition").withIndex(11).ofType(Types.VARCHAR).withSize(256).notNull());
        addMetadata(ilpPacket, ColumnMetadata.named("ilpPacket").withIndex(15).ofType(Types.LONGVARCHAR).withSize(65535).notNull());
        addMetadata(isValid, ColumnMetadata.named("isValid").withIndex(13).ofType(Types.BIT).withSize(1));
        addMetadata(latitude, ColumnMetadata.named("latitude").withIndex(17).ofType(Types.VARCHAR).withSize(50));
        addMetadata(longitude, ColumnMetadata.named("longitude").withIndex(16).ofType(Types.VARCHAR).withSize(50));
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

