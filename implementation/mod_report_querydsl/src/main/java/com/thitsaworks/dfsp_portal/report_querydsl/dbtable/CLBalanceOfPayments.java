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
 * CLBalanceOfPayments is a Querydsl query type for CLBalanceOfPayments
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLBalanceOfPayments extends com.querydsl.sql.RelationalPathBase<CLBalanceOfPayments> {

    private static final long serialVersionUID = 322180739;

    public static final CLBalanceOfPayments balanceOfPayments = new CLBalanceOfPayments("balanceOfPayments");

    public final NumberPath<Integer> balanceOfPaymentsId = createNumber("balanceOfPaymentsId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<CLBalanceOfPayments> primary = createPrimaryKey(balanceOfPaymentsId);

    public final com.querydsl.sql.ForeignKey<CLQuote> _quoteBalanceofpaymentsidForeign = createInvForeignKey(Arrays.asList(balanceOfPaymentsId, balanceOfPaymentsId), Arrays.asList("balanceOfPaymentsId", "balanceOfPaymentsId"));

    public CLBalanceOfPayments(String variable) {
        super(CLBalanceOfPayments.class, forVariable(variable), "null", "balanceOfPayments");
        addMetadata();
    }

    public CLBalanceOfPayments(String variable, String schema, String table) {
        super(CLBalanceOfPayments.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLBalanceOfPayments(String variable, String schema) {
        super(CLBalanceOfPayments.class, forVariable(variable), schema, "balanceOfPayments");
        addMetadata();
    }

    public CLBalanceOfPayments(Path<? extends CLBalanceOfPayments> path) {
        super(path.getType(), path.getMetadata(), "null", "balanceOfPayments");
        addMetadata();
    }

    public CLBalanceOfPayments(PathMetadata metadata) {
        super(CLBalanceOfPayments.class, metadata, "null", "balanceOfPayments");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(balanceOfPaymentsId, ColumnMetadata.named("balanceOfPaymentsId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(4).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(1024));
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(256).notNull());
    }

}

