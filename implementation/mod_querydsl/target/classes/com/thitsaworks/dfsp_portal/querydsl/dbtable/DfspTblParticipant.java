package com.thitsaworks.dfsp_portal.querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * DfspTblParticipant is a Querydsl query type for DfspTblParticipant
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class DfspTblParticipant extends com.querydsl.sql.RelationalPathBase<DfspTblParticipant> {

    private static final long serialVersionUID = -215594973;

    public static final DfspTblParticipant tblParticipant = new DfspTblParticipant("tbl_participant");

    public final StringPath address = createString("address");

    public final NumberPath<Long> businessContactId = createNumber("businessContactId", Long.class);

    public final NumberPath<Long> createdDate = createNumber("createdDate", Long.class);

    public final StringPath dfspCode = createString("dfspCode");

    public final StringPath dfspName = createString("dfspName");

    public final StringPath mobile = createString("mobile");

    public final StringPath name = createString("name");

    public final NumberPath<Long> participantId = createNumber("participantId", Long.class);

    public final NumberPath<Long> technicalContactId = createNumber("technicalContactId", Long.class);

    public final NumberPath<Long> updatedDate = createNumber("updatedDate", Long.class);

    public final com.querydsl.sql.PrimaryKey<DfspTblParticipant> primary = createPrimaryKey(participantId);

    public DfspTblParticipant(String variable) {
        super(DfspTblParticipant.class, forVariable(variable), "null", "tbl_participant");
        addMetadata();
    }

    public DfspTblParticipant(String variable, String schema, String table) {
        super(DfspTblParticipant.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public DfspTblParticipant(String variable, String schema) {
        super(DfspTblParticipant.class, forVariable(variable), schema, "tbl_participant");
        addMetadata();
    }

    public DfspTblParticipant(Path<? extends DfspTblParticipant> path) {
        super(path.getType(), path.getMetadata(), "null", "tbl_participant");
        addMetadata();
    }

    public DfspTblParticipant(PathMetadata metadata) {
        super(DfspTblParticipant.class, metadata, "null", "tbl_participant");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(address, ColumnMetadata.named("address").withIndex(5).ofType(Types.VARCHAR).withSize(500));
        addMetadata(businessContactId, ColumnMetadata.named("business_contact_id").withIndex(9).ofType(Types.BIGINT).withSize(19));
        addMetadata(createdDate, ColumnMetadata.named("created_date").withIndex(7).ofType(Types.BIGINT).withSize(19));
        addMetadata(dfspCode, ColumnMetadata.named("dfsp_code").withIndex(2).ofType(Types.VARCHAR).withSize(100));
        addMetadata(dfspName, ColumnMetadata.named("dfsp_name").withIndex(4).ofType(Types.VARCHAR).withSize(100));
        addMetadata(mobile, ColumnMetadata.named("mobile").withIndex(6).ofType(Types.VARCHAR).withSize(1000));
        addMetadata(name, ColumnMetadata.named("name").withIndex(3).ofType(Types.VARCHAR).withSize(100));
        addMetadata(participantId, ColumnMetadata.named("participant_id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(technicalContactId, ColumnMetadata.named("technical_contact_id").withIndex(10).ofType(Types.BIGINT).withSize(19));
        addMetadata(updatedDate, ColumnMetadata.named("updated_date").withIndex(8).ofType(Types.BIGINT).withSize(19));
    }

}

