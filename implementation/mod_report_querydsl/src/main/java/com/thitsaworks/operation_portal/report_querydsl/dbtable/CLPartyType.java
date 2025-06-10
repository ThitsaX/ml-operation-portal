package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import java.util.*;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLPartyType is a Querydsl query type for CLPartyType
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLPartyType extends com.querydsl.sql.RelationalPathBase<CLPartyType> {

    private static final long serialVersionUID = -1930607965;

    public static final CLPartyType partyType = new CLPartyType("partyType");

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> partyTypeId = createNumber("partyTypeId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLPartyType> primary = createPrimaryKey(partyTypeId);

    public final com.querydsl.sql.ForeignKey<CLQuoteParty> _quotepartyPartytypeidForeign = createInvForeignKey(Arrays.asList(partyTypeId, partyTypeId), Arrays.asList("partyTypeId", "partyTypeId"));

    public CLPartyType(String variable) {
        super(CLPartyType.class, forVariable(variable), "null", "partyType");
        addMetadata();
    }

    public CLPartyType(String variable, String schema, String table) {
        super(CLPartyType.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLPartyType(String variable, String schema) {
        super(CLPartyType.class, forVariable(variable), schema, "partyType");
        addMetadata();
    }

    public CLPartyType(Path<? extends CLPartyType> path) {
        super(path.getType(), path.getMetadata(), "null", "partyType");
        addMetadata();
    }

    public CLPartyType(PathMetadata metadata) {
        super(CLPartyType.class, metadata, "null", "partyType");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(256).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(partyTypeId, ColumnMetadata.named("partyTypeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

