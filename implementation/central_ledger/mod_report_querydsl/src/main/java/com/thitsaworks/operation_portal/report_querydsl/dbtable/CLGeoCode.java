package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLGeoCode is a Querydsl query type for CLGeoCode
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLGeoCode extends com.querydsl.sql.RelationalPathBase<CLGeoCode> {

    private static final long serialVersionUID = -1729551103;

    public static final CLGeoCode geoCode = new CLGeoCode("geoCode");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> geoCodeId = createNumber("geoCodeId", Integer.class);

    public final StringPath latitude = createString("latitude");

    public final StringPath longitude = createString("longitude");

    public final NumberPath<Long> quotePartyId = createNumber("quotePartyId", Long.class);

    public final com.querydsl.sql.PrimaryKey<CLGeoCode> primary = createPrimaryKey(geoCodeId);

    public final com.querydsl.sql.ForeignKey<CLQuoteParty> geocodeQuotepartyidForeign = createForeignKey(quotePartyId, "quotePartyId");

    public CLGeoCode(String variable) {
        super(CLGeoCode.class, forVariable(variable), "null", "geoCode");
        addMetadata();
    }

    public CLGeoCode(String variable, String schema, String table) {
        super(CLGeoCode.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLGeoCode(String variable, String schema) {
        super(CLGeoCode.class, forVariable(variable), schema, "geoCode");
        addMetadata();
    }

    public CLGeoCode(Path<? extends CLGeoCode> path) {
        super(path.getType(), path.getMetadata(), "null", "geoCode");
        addMetadata();
    }

    public CLGeoCode(PathMetadata metadata) {
        super(CLGeoCode.class, metadata, "null", "geoCode");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(geoCodeId, ColumnMetadata.named("geoCodeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(latitude, ColumnMetadata.named("latitude").withIndex(3).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(longitude, ColumnMetadata.named("longitude").withIndex(4).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(quotePartyId, ColumnMetadata.named("quotePartyId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
    }

}

