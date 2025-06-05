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
 * CLTransactionScenario is a Querydsl query type for CLTransactionScenario
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransactionScenario extends com.querydsl.sql.RelationalPathBase<CLTransactionScenario> {

    private static final long serialVersionUID = 857494577;

    public static final CLTransactionScenario transactionScenario = new CLTransactionScenario("transactionScenario");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> transactionScenarioId = createNumber("transactionScenarioId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLTransactionScenario> primary = createPrimaryKey(transactionScenarioId);

    public final com.querydsl.sql.ForeignKey<CLQuote> _quoteTransactionscenarioidForeign = createInvForeignKey(Arrays.asList(transactionScenarioId, transactionScenarioId), Arrays.asList("transactionScenarioId", "transactionScenarioId"));

    public CLTransactionScenario(String variable) {
        super(CLTransactionScenario.class, forVariable(variable), "null", "transactionScenario");
        addMetadata();
    }

    public CLTransactionScenario(String variable, String schema, String table) {
        super(CLTransactionScenario.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransactionScenario(String variable, String schema) {
        super(CLTransactionScenario.class, forVariable(variable), schema, "transactionScenario");
        addMetadata();
    }

    public CLTransactionScenario(Path<? extends CLTransactionScenario> path) {
        super(path.getType(), path.getMetadata(), "null", "transactionScenario");
        addMetadata();
    }

    public CLTransactionScenario(PathMetadata metadata) {
        super(CLTransactionScenario.class, metadata, "null", "transactionScenario");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(1024));
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(256).notNull());
        addMetadata(transactionScenarioId, ColumnMetadata.named("transactionScenarioId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

