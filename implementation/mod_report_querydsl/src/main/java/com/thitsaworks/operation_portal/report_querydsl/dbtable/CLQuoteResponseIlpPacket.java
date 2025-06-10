package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLQuoteResponseIlpPacket is a Querydsl query type for CLQuoteResponseIlpPacket
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLQuoteResponseIlpPacket extends com.querydsl.sql.RelationalPathBase<CLQuoteResponseIlpPacket> {

    private static final long serialVersionUID = 1444182453;

    public static final CLQuoteResponseIlpPacket quoteResponseIlpPacket = new CLQuoteResponseIlpPacket("quoteResponseIlpPacket");

    public final NumberPath<Long> quoteResponseId = createNumber("quoteResponseId", Long.class);

    public final StringPath value = createString("value");

    public final com.querydsl.sql.PrimaryKey<CLQuoteResponseIlpPacket> primary = createPrimaryKey(quoteResponseId);

    public final com.querydsl.sql.ForeignKey<CLQuoteResponse> quoteresponseilppacketQuoteresponseidForeign = createForeignKey(quoteResponseId, "quoteResponseId");

    public CLQuoteResponseIlpPacket(String variable) {
        super(CLQuoteResponseIlpPacket.class, forVariable(variable), "null", "quoteResponseIlpPacket");
        addMetadata();
    }

    public CLQuoteResponseIlpPacket(String variable, String schema, String table) {
        super(CLQuoteResponseIlpPacket.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLQuoteResponseIlpPacket(String variable, String schema) {
        super(CLQuoteResponseIlpPacket.class, forVariable(variable), schema, "quoteResponseIlpPacket");
        addMetadata();
    }

    public CLQuoteResponseIlpPacket(Path<? extends CLQuoteResponseIlpPacket> path) {
        super(path.getType(), path.getMetadata(), "null", "quoteResponseIlpPacket");
        addMetadata();
    }

    public CLQuoteResponseIlpPacket(PathMetadata metadata) {
        super(CLQuoteResponseIlpPacket.class, metadata, "null", "quoteResponseIlpPacket");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(quoteResponseId, ColumnMetadata.named("quoteResponseId").withIndex(1).ofType(Types.BIGINT).withSize(20).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(2).ofType(Types.LONGVARCHAR).withSize(65535).notNull());
    }

}

