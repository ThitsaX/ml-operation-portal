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
 * CLAmountType is a Querydsl query type for CLAmountType
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLAmountType extends com.querydsl.sql.RelationalPathBase<CLAmountType> {

    private static final long serialVersionUID = -1613607761;

    public static final CLAmountType amountType = new CLAmountType("amountType");

    public final NumberPath<Integer> amountTypeId = createNumber("amountTypeId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<CLAmountType> primary = createPrimaryKey(amountTypeId);

    public final com.querydsl.sql.ForeignKey<CLQuote> _quoteAmounttypeidForeign = createInvForeignKey(Arrays.asList(amountTypeId, amountTypeId), Arrays.asList("amountTypeId", "amountTypeId"));

    public CLAmountType(String variable) {
        super(CLAmountType.class, forVariable(variable), "null", "amountType");
        addMetadata();
    }

    public CLAmountType(String variable, String schema, String table) {
        super(CLAmountType.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLAmountType(String variable, String schema) {
        super(CLAmountType.class, forVariable(variable), schema, "amountType");
        addMetadata();
    }

    public CLAmountType(Path<? extends CLAmountType> path) {
        super(path.getType(), path.getMetadata(), "null", "amountType");
        addMetadata();
    }

    public CLAmountType(PathMetadata metadata) {
        super(CLAmountType.class, metadata, "null", "amountType");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(amountTypeId, ColumnMetadata.named("amountTypeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(1024));
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(256).notNull());
    }

}

