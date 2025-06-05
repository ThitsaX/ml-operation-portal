package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransferRules is a Querydsl query type for CLTransferRules
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransferRules extends com.querydsl.sql.RelationalPathBase<CLTransferRules> {

    private static final long serialVersionUID = 1312258479;

    public static final CLTransferRules transferRules = new CLTransferRules("transferRules");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final BooleanPath enabled = createBoolean("enabled");

    public final StringPath name = createString("name");

    public final StringPath rule = createString("rule");

    public final NumberPath<Integer> transferRulesId = createNumber("transferRulesId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLTransferRules> primary = createPrimaryKey(transferRulesId);

    public CLTransferRules(String variable) {
        super(CLTransferRules.class, forVariable(variable), "null", "transferRules");
        addMetadata();
    }

    public CLTransferRules(String variable, String schema, String table) {
        super(CLTransferRules.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransferRules(String variable, String schema) {
        super(CLTransferRules.class, forVariable(variable), schema, "transferRules");
        addMetadata();
    }

    public CLTransferRules(Path<? extends CLTransferRules> path) {
        super(path.getType(), path.getMetadata(), "null", "transferRules");
        addMetadata();
    }

    public CLTransferRules(PathMetadata metadata) {
        super(CLTransferRules.class, metadata, "null", "transferRules");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(6).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(enabled, ColumnMetadata.named("enabled").withIndex(5).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(rule, ColumnMetadata.named("rule").withIndex(4).ofType(Types.LONGVARCHAR).withSize(65535).notNull());
        addMetadata(transferRulesId, ColumnMetadata.named("transferRulesId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

