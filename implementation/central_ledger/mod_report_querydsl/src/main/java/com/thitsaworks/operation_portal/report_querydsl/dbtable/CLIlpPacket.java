package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLIlpPacket is a Querydsl query type for CLIlpPacket
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLIlpPacket extends com.querydsl.sql.RelationalPathBase<CLIlpPacket> {

    private static final long serialVersionUID = 1295294968;

    public static final CLIlpPacket ilpPacket = new CLIlpPacket("ilpPacket");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath transferId = createString("transferId");

    public final StringPath value = createString("value");

    public final com.querydsl.sql.PrimaryKey<CLIlpPacket> primary = createPrimaryKey(transferId);

    public final com.querydsl.sql.ForeignKey<CLTransfer> ilppacketTransferidForeign = createForeignKey(transferId, "transferId");

    public CLIlpPacket(String variable) {
        super(CLIlpPacket.class, forVariable(variable), "null", "ilpPacket");
        addMetadata();
    }

    public CLIlpPacket(String variable, String schema, String table) {
        super(CLIlpPacket.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLIlpPacket(String variable, String schema) {
        super(CLIlpPacket.class, forVariable(variable), schema, "ilpPacket");
        addMetadata();
    }

    public CLIlpPacket(Path<? extends CLIlpPacket> path) {
        super(path.getType(), path.getMetadata(), "null", "ilpPacket");
        addMetadata();
    }

    public CLIlpPacket(PathMetadata metadata) {
        super(CLIlpPacket.class, metadata, "null", "ilpPacket");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(3).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(transferId, ColumnMetadata.named("transferId").withIndex(1).ofType(Types.VARCHAR).withSize(36).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(2).ofType(Types.LONGVARCHAR).withSize(65535).notNull());
    }

}

