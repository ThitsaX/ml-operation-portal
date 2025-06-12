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
 * CLPartyIdentifierType is a Querydsl query type for CLPartyIdentifierType
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLPartyIdentifierType extends com.querydsl.sql.RelationalPathBase<CLPartyIdentifierType> {

    private static final long serialVersionUID = -1194821716;

    public static final CLPartyIdentifierType partyIdentifierType = new CLPartyIdentifierType("partyIdentifierType");

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> partyIdentifierTypeId = createNumber("partyIdentifierTypeId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLPartyIdentifierType> primary = createPrimaryKey(partyIdentifierTypeId);

    public final com.querydsl.sql.ForeignKey<CLQuoteParty> _quotepartyPartyidentifiertypeidForeign = createInvForeignKey(Arrays.asList(partyIdentifierTypeId, partyIdentifierTypeId), Arrays.asList("partyIdentifierTypeId", "partyIdentifierTypeId"));

    public CLPartyIdentifierType(String variable) {
        super(CLPartyIdentifierType.class, forVariable(variable), "null", "partyIdentifierType");
        addMetadata();
    }

    public CLPartyIdentifierType(String variable, String schema, String table) {
        super(CLPartyIdentifierType.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLPartyIdentifierType(String variable, String schema) {
        super(CLPartyIdentifierType.class, forVariable(variable), schema, "partyIdentifierType");
        addMetadata();
    }

    public CLPartyIdentifierType(Path<? extends CLPartyIdentifierType> path) {
        super(path.getType(), path.getMetadata(), "null", "partyIdentifierType");
        addMetadata();
    }

    public CLPartyIdentifierType(PathMetadata metadata) {
        super(CLPartyIdentifierType.class, metadata, "null", "partyIdentifierType");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(partyIdentifierTypeId, ColumnMetadata.named("partyIdentifierTypeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

