package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import java.util.*;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLSettlementModel is a Querydsl query type for CLSettlementModel
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLSettlementModel extends com.querydsl.sql.RelationalPathBase<CLSettlementModel> {

    private static final long serialVersionUID = -923302749;

    public static final CLSettlementModel settlementModel = new CLSettlementModel("settlementModel");

    public final BooleanPath adjustPosition = createBoolean("adjustPosition");

    public final BooleanPath autoPositionReset = createBoolean("autoPositionReset");

    public final StringPath currencyId = createString("currencyId");

    public final BooleanPath isActive = createBoolean("isActive");

    public final NumberPath<Integer> ledgerAccountTypeId = createNumber("ledgerAccountTypeId", Integer.class);

    public final StringPath name = createString("name");

    public final BooleanPath requireLiquidityCheck = createBoolean("requireLiquidityCheck");

    public final NumberPath<Integer> settlementAccountTypeId = createNumber("settlementAccountTypeId", Integer.class);

    public final NumberPath<Integer> settlementDelayId = createNumber("settlementDelayId", Integer.class);

    public final NumberPath<Integer> settlementGranularityId = createNumber("settlementGranularityId", Integer.class);

    public final NumberPath<Integer> settlementInterchangeId = createNumber("settlementInterchangeId", Integer.class);

    public final NumberPath<Integer> settlementModelId = createNumber("settlementModelId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLSettlementModel> primary = createPrimaryKey(settlementModelId);

    public final com.querydsl.sql.ForeignKey<CLCurrency> settlementmodelCurrencyidForeign = createForeignKey(currencyId, "currencyId");

    public final com.querydsl.sql.ForeignKey<CLLedgerAccountType> settlementmodelLedgeraccounttypeidForeign = createForeignKey(ledgerAccountTypeId, "ledgerAccountTypeId");

    public final com.querydsl.sql.ForeignKey<CLSettlementDelay> settlementmodelSettlementdelayidForeign = createForeignKey(settlementDelayId, "settlementDelayId");

    public final com.querydsl.sql.ForeignKey<CLSettlementGranularity> settlementmodelSettlementgranularityidForeign = createForeignKey(settlementGranularityId, "settlementGranularityId");

    public final com.querydsl.sql.ForeignKey<CLSettlementInterchange> settlementmodelSettlementinterchangeidForeign = createForeignKey(settlementInterchangeId, "settlementInterchangeId");

    public final com.querydsl.sql.ForeignKey<CLSettlement> _settlementSettlementmodelidForeign = createInvForeignKey(Arrays.asList(settlementModelId, settlementModelId, settlementModelId, settlementModelId, settlementModelId), Arrays.asList("settlementModelId", "settlementModelId", "settlementModelId", "settlementModelId", "settlementModelId"));

    public final com.querydsl.sql.ForeignKey<CLSettlementWindowContent> _settlementwindowcontentSettlementmodelidForeign = createInvForeignKey(Arrays.asList(settlementModelId, settlementModelId, settlementModelId, settlementModelId, settlementModelId), Arrays.asList("SettlementModelId", "SettlementModelId", "SettlementModelId", "SettlementModelId", "SettlementModelId"));

    public CLSettlementModel(String variable) {
        super(CLSettlementModel.class, forVariable(variable), "null", "settlementModel");
        addMetadata();
    }

    public CLSettlementModel(String variable, String schema, String table) {
        super(CLSettlementModel.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLSettlementModel(String variable, String schema) {
        super(CLSettlementModel.class, forVariable(variable), schema, "settlementModel");
        addMetadata();
    }

    public CLSettlementModel(Path<? extends CLSettlementModel> path) {
        super(path.getType(), path.getMetadata(), "null", "settlementModel");
        addMetadata();
    }

    public CLSettlementModel(PathMetadata metadata) {
        super(CLSettlementModel.class, metadata, "null", "settlementModel");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(adjustPosition, ColumnMetadata.named("adjustPosition").withIndex(11).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(autoPositionReset, ColumnMetadata.named("autoPositionReset").withIndex(10).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(currencyId, ColumnMetadata.named("currencyId").withIndex(7).ofType(Types.VARCHAR).withSize(3));
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(3).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(ledgerAccountTypeId, ColumnMetadata.named("ledgerAccountTypeId").withIndex(9).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(requireLiquidityCheck, ColumnMetadata.named("requireLiquidityCheck").withIndex(8).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(settlementAccountTypeId, ColumnMetadata.named("settlementAccountTypeId").withIndex(12).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(settlementDelayId, ColumnMetadata.named("settlementDelayId").withIndex(6).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(settlementGranularityId, ColumnMetadata.named("settlementGranularityId").withIndex(4).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(settlementInterchangeId, ColumnMetadata.named("settlementInterchangeId").withIndex(5).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(settlementModelId, ColumnMetadata.named("settlementModelId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

