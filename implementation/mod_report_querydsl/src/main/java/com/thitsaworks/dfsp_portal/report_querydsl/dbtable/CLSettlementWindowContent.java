package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSettlementWindowContent is a Querydsl query type for CLSettlementWindowContent
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementWindowContent extends com.querydsl.sql.RelationalPathBase<CLSettlementWindowContent> {

    private static final long serialVersionUID = 885592547;

    public static final CLSettlementWindowContent settlementWindowContent = new CLSettlementWindowContent("settlementWindowContent");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath currencyId = createString("currencyId");

    public final NumberPath<Long> currentStateChangeId = createNumber("currentStateChangeId", Long.class);

    public final NumberPath<Integer> ledgerAccountTypeId = createNumber("ledgerAccountTypeId", Integer.class);

    public final NumberPath<Long> settlementId = createNumber("settlementId", Long.class);

    public final NumberPath<Integer> settlementModelId = createNumber("settlementModelId", Integer.class);

    public final NumberPath<Long> settlementWindowContentId = createNumber("settlementWindowContentId", Long.class);

    public final NumberPath<Long> settlementWindowId = createNumber("settlementWindowId", Long.class);

    public final com.querydsl.sql.PrimaryKey<CLSettlementWindowContent> primary = createPrimaryKey(settlementWindowContentId);

    public final com.querydsl.sql.ForeignKey<CLCurrency> settlementwindowcontentCurrencyidForeign = createForeignKey(currencyId, "currencyId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowContentStateChange> settlementwindowcontentCurrentstatechangeidForeign = createForeignKey(currentStateChangeId, "settlementWindowContentStateChangeId");

    public final com.querydsl.sql.ForeignKey<CLLedgerAccountType> settlementwindowcontentLedgeraccounttypeidForeign = createForeignKey(ledgerAccountTypeId, "ledgerAccountTypeId");

    public final com.querydsl.sql.ForeignKey<CLSettlement> settlementwindowcontentSettlementidForeign = createForeignKey(settlementId, "settlementId");

    public final com.querydsl.sql.ForeignKey<CLSettlementModel> settlementwindowcontentSettlementmodelidForeign = createForeignKey(settlementModelId, "settlementModelId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindow> settlementwindowcontentSettlementwindowidForeign = createForeignKey(settlementWindowId, "settlementWindowId");

    public final com.querydsl.sql.ForeignKey<CLSettlementContentAggregation> _settlementcontentaggregationSettlementwindowcontentidForeign = createInvForeignKey(settlementWindowContentId, "settlementWindowContentId");

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowContentStateChange> _swcSettlementwindowcontentidForeign = createInvForeignKey(settlementWindowContentId, "settlementWindowContentId");

    public CLSettlementWindowContent(String variable) {
        super(CLSettlementWindowContent.class, forVariable(variable), "null", "settlementWindowContent");
        addMetadata();
    }

    public CLSettlementWindowContent(String variable, String schema, String table) {
        super(CLSettlementWindowContent.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementWindowContent(String variable, String schema) {
        super(CLSettlementWindowContent.class, forVariable(variable), schema, "settlementWindowContent");
        addMetadata();
    }

    public CLSettlementWindowContent(Path<? extends CLSettlementWindowContent> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementWindowContent");
        addMetadata();
    }

    public CLSettlementWindowContent(PathMetadata metadata) {
        super(CLSettlementWindowContent.class, metadata, "null", "settlementWindowContent");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(currencyId, ColumnMetadata.named("currencyId").withIndex(4).ofType(Types.VARCHAR).withSize(3).notNull());
        addMetadata(currentStateChangeId, ColumnMetadata.named("currentStateChangeId").withIndex(6).ofType(Types.BIGINT).withSize(20));
        addMetadata(ledgerAccountTypeId, ColumnMetadata.named("ledgerAccountTypeId").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(settlementId, ColumnMetadata.named("settlementId").withIndex(7).ofType(Types.BIGINT).withSize(20));
        addMetadata(settlementModelId, ColumnMetadata.named("SettlementModelId").withIndex(8).ofType(Types.INTEGER).withSize(10));
        addMetadata(settlementWindowContentId, ColumnMetadata.named("settlementWindowContentId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(settlementWindowId, ColumnMetadata.named("settlementWindowId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
    }

}

