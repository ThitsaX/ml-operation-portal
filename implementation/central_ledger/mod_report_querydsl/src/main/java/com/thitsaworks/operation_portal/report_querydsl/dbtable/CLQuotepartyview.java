package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLQuotepartyview is a Querydsl query type for CLQuotepartyview
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLQuotepartyview extends com.querydsl.sql.RelationalPathBase<CLQuotepartyview> {

    private static final long serialVersionUID = -1860182516;

    public static final CLQuotepartyview quotepartyview = new CLQuotepartyview("quotepartyview");

    public final DateTimePath<java.sql.Timestamp> dateOfBirth = createDateTime("dateOfBirth", java.sql.Timestamp.class);

    public final StringPath firstName = createString("firstName");

    public final StringPath fspId = createString("fspId");

    public final StringPath identifierType = createString("identifierType");

    public final StringPath lastName = createString("lastName");

    public final StringPath latitude = createString("latitude");

    public final StringPath longitude = createString("longitude");

    public final StringPath merchantClassificationCode = createString("merchantClassificationCode");

    public final StringPath middleName = createString("middleName");

    public final StringPath partyIdentifierValue = createString("partyIdentifierValue");

    public final StringPath partyName = createString("partyName");

    public final StringPath partySubIdOrType = createString("partySubIdOrType");

    public final StringPath partyType = createString("partyType");

    public final StringPath quoteId = createString("quoteId");

    public final NumberPath<Long> quotePartyId = createNumber("quotePartyId", Long.class);

    public CLQuotepartyview(String variable) {
        super(CLQuotepartyview.class, forVariable(variable), "null", "quotepartyview");
        addMetadata();
    }

    public CLQuotepartyview(String variable, String schema, String table) {
        super(CLQuotepartyview.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLQuotepartyview(String variable, String schema) {
        super(CLQuotepartyview.class, forVariable(variable), schema, "quotepartyview");
        addMetadata();
    }

    public CLQuotepartyview(Path<? extends CLQuotepartyview> path) {
        super(path.getType(), path.getMetadata(), "null", "quotepartyview");
        addMetadata();
    }

    public CLQuotepartyview(PathMetadata metadata) {
        super(CLQuotepartyview.class, metadata, "null", "quotepartyview");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(dateOfBirth, ColumnMetadata.named("dateOfBirth").withIndex(13).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(firstName, ColumnMetadata.named("firstName").withIndex(10).ofType(Types.VARCHAR).withSize(128));
        addMetadata(fspId, ColumnMetadata.named("fspId").withIndex(7).ofType(Types.VARCHAR).withSize(255));
        addMetadata(identifierType, ColumnMetadata.named("identifierType").withIndex(4).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(lastName, ColumnMetadata.named("lastName").withIndex(11).ofType(Types.VARCHAR).withSize(128));
        addMetadata(latitude, ColumnMetadata.named("latitude").withIndex(15).ofType(Types.VARCHAR).withSize(50));
        addMetadata(longitude, ColumnMetadata.named("longitude").withIndex(14).ofType(Types.VARCHAR).withSize(50));
        addMetadata(merchantClassificationCode, ColumnMetadata.named("merchantClassificationCode").withIndex(8).ofType(Types.VARCHAR).withSize(4));
        addMetadata(middleName, ColumnMetadata.named("middleName").withIndex(12).ofType(Types.VARCHAR).withSize(128));
        addMetadata(partyIdentifierValue, ColumnMetadata.named("partyIdentifierValue").withIndex(5).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(partyName, ColumnMetadata.named("partyName").withIndex(9).ofType(Types.VARCHAR).withSize(128));
        addMetadata(partySubIdOrType, ColumnMetadata.named("partySubIdOrType").withIndex(6).ofType(Types.VARCHAR).withSize(128));
        addMetadata(partyType, ColumnMetadata.named("partyType").withIndex(3).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(quoteId, ColumnMetadata.named("quoteId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(quotePartyId, ColumnMetadata.named("quotePartyId").withIndex(2).ofType(Types.BIGINT).withSize(20).notNull());
    }

}

