package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTransactionSubScenario is a Querydsl query type for CLTransactionSubScenario
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransactionSubScenario extends com.querydsl.sql.RelationalPathBase<CLTransactionSubScenario> {

    private static final long serialVersionUID = -1658477137;

    public static final CLTransactionSubScenario transactionSubScenario = new CLTransactionSubScenario("transactionSubScenario");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> transactionSubScenarioId = createNumber("transactionSubScenarioId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLTransactionSubScenario> primary = createPrimaryKey(transactionSubScenarioId);

    public final com.querydsl.sql.ForeignKey<CLQuote> _quoteTransactionsubscenarioidForeign = createInvForeignKey(transactionSubScenarioId, "transactionSubScenarioId");

    public CLTransactionSubScenario(String variable) {
        super(CLTransactionSubScenario.class, forVariable(variable), "null", "transactionSubScenario");
        addMetadata();
    }

    public CLTransactionSubScenario(String variable, String schema, String table) {
        super(CLTransactionSubScenario.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransactionSubScenario(String variable, String schema) {
        super(CLTransactionSubScenario.class, forVariable(variable), schema, "transactionSubScenario");
        addMetadata();
    }

    public CLTransactionSubScenario(Path<? extends CLTransactionSubScenario> path) {
        super(path.getType(), path.getMetadata(), "null", "transactionSubScenario");
        addMetadata();
    }

    public CLTransactionSubScenario(PathMetadata metadata) {
        super(CLTransactionSubScenario.class, metadata, "null", "transactionSubScenario");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(1024));
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(256).notNull());
        addMetadata(transactionSubScenarioId, ColumnMetadata.named("transactionSubScenarioId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

