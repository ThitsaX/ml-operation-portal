package com.thitsaworks.dfsp_portal.querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * DfspTblLiquidityProfile is a Querydsl query type for DfspTblLiquidityProfile
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class DfspTblLiquidityProfile extends com.querydsl.sql.RelationalPathBase<DfspTblLiquidityProfile> {

    private static final long serialVersionUID = -176974745;

    public static final DfspTblLiquidityProfile tblLiquidityProfile = new DfspTblLiquidityProfile("tbl_liquidity_profile");

    public final StringPath accountName = createString("accountName");

    public final StringPath accountNumber = createString("accountNumber");

    public final NumberPath<Long> createdDate = createNumber("createdDate", Long.class);

    public final StringPath currency = createString("currency");

    public final BooleanPath isActive = createBoolean("isActive");

    public final NumberPath<Long> liquidityProfileId = createNumber("liquidityProfileId", Long.class);

    public final NumberPath<Long> participantId = createNumber("participantId", Long.class);

    public final NumberPath<Long> updatedDate = createNumber("updatedDate", Long.class);

    public DfspTblLiquidityProfile(String variable) {
        super(DfspTblLiquidityProfile.class, forVariable(variable), "null", "tbl_liquidity_profile");
        addMetadata();
    }

    public DfspTblLiquidityProfile(String variable, String schema, String table) {
        super(DfspTblLiquidityProfile.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public DfspTblLiquidityProfile(String variable, String schema) {
        super(DfspTblLiquidityProfile.class, forVariable(variable), schema, "tbl_liquidity_profile");
        addMetadata();
    }

    public DfspTblLiquidityProfile(Path<? extends DfspTblLiquidityProfile> path) {
        super(path.getType(), path.getMetadata(), "null", "tbl_liquidity_profile");
        addMetadata();
    }

    public DfspTblLiquidityProfile(PathMetadata metadata) {
        super(DfspTblLiquidityProfile.class, metadata, "null", "tbl_liquidity_profile");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(accountName, ColumnMetadata.named("account_name").withIndex(3).ofType(Types.VARCHAR).withSize(200));
        addMetadata(accountNumber, ColumnMetadata.named("account_number").withIndex(4).ofType(Types.VARCHAR).withSize(200));
        addMetadata(createdDate, ColumnMetadata.named("created_date").withIndex(7).ofType(Types.BIGINT).withSize(19));
        addMetadata(currency, ColumnMetadata.named("currency").withIndex(5).ofType(Types.VARCHAR).withSize(10));
        addMetadata(isActive, ColumnMetadata.named("is_active").withIndex(6).ofType(Types.BIT).withSize(1));
        addMetadata(liquidityProfileId, ColumnMetadata.named("liquidity_profile_id").withIndex(1).ofType(Types.BIGINT).withSize(19));
        addMetadata(participantId, ColumnMetadata.named("participant_id").withIndex(2).ofType(Types.BIGINT).withSize(19));
        addMetadata(updatedDate, ColumnMetadata.named("updated_date").withIndex(8).ofType(Types.BIGINT).withSize(19));
    }

}

