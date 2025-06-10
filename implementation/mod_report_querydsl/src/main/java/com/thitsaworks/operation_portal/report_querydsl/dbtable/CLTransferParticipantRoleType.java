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
 * CLTransferParticipantRoleType is a Querydsl query type for CLTransferParticipantRoleType
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTransferParticipantRoleType extends com.querydsl.sql.RelationalPathBase<CLTransferParticipantRoleType> {

    private static final long serialVersionUID = 1976719323;

    public static final CLTransferParticipantRoleType transferParticipantRoleType = new CLTransferParticipantRoleType("transferParticipantRoleType");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final StringPath description = createString("description");

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> transferParticipantRoleTypeId = createNumber("transferParticipantRoleTypeId", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLTransferParticipantRoleType> primary = createPrimaryKey(transferParticipantRoleTypeId);

    public final com.querydsl.sql.ForeignKey<CLQuoteParty> _quotepartyTransferparticipantroletypeidForeign = createInvForeignKey(Arrays.asList(transferParticipantRoleTypeId, transferParticipantRoleTypeId), Arrays.asList("transferParticipantRoleTypeId", "transferParticipantRoleTypeId"));

    public final com.querydsl.sql.ForeignKey<CLSettlementContentAggregation> _scaTransferparticipantroletypeidForeign = createInvForeignKey(Arrays.asList(transferParticipantRoleTypeId, transferParticipantRoleTypeId), Arrays.asList("transferParticipantRoleTypeId", "transferParticipantRoleTypeId"));

    public final com.querydsl.sql.ForeignKey<CLSettlementTransferParticipant> _stpTransferparticipantroletypeidForeign = createInvForeignKey(Arrays.asList(transferParticipantRoleTypeId, transferParticipantRoleTypeId), Arrays.asList("transferParticipantRoleTypeId", "transferParticipantRoleTypeId"));

    public final com.querydsl.sql.ForeignKey<CLTransferParticipant> _transferparticipantTransferparticipantroletypeidForeign = createInvForeignKey(Arrays.asList(transferParticipantRoleTypeId, transferParticipantRoleTypeId), Arrays.asList("transferParticipantRoleTypeId", "transferParticipantRoleTypeId"));

    public CLTransferParticipantRoleType(String variable) {
        super(CLTransferParticipantRoleType.class, forVariable(variable), "null", "transferParticipantRoleType");
        addMetadata();
    }

    public CLTransferParticipantRoleType(String variable, String schema, String table) {
        super(CLTransferParticipantRoleType.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTransferParticipantRoleType(String variable, String schema) {
        super(CLTransferParticipantRoleType.class, forVariable(variable), schema, "transferParticipantRoleType");
        addMetadata();
    }

    public CLTransferParticipantRoleType(Path<? extends CLTransferParticipantRoleType> path) {
        super(path.getType(), path.getMetadata(), "null", "transferParticipantRoleType");
        addMetadata();
    }

    public CLTransferParticipantRoleType(PathMetadata metadata) {
        super(CLTransferParticipantRoleType.class, metadata, "null", "transferParticipantRoleType");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(5).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(description, ColumnMetadata.named("description").withIndex(3).ofType(Types.VARCHAR).withSize(512));
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(4).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(transferParticipantRoleTypeId, ColumnMetadata.named("transferParticipantRoleTypeId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
    }

}

