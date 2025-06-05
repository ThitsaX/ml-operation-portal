package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTblParticipant is a Querydsl query type for CLTblParticipant
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTblParticipant extends com.querydsl.sql.RelationalPathBase<CLTblParticipant> {

    private static final long serialVersionUID = -1898163438;

    public static final CLTblParticipant tblParticipant = new CLTblParticipant("tbl_participant");

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

    public final com.querydsl.sql.PrimaryKey<CLTblParticipant> primary = createPrimaryKey(participantId);

    public CLTblParticipant(String variable) {
        super(CLTblParticipant.class, forVariable(variable), "null", "tbl_participant");
        addMetadata();
    }

    public CLTblParticipant(String variable, String schema, String table) {
        super(CLTblParticipant.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTblParticipant(String variable, String schema) {
        super(CLTblParticipant.class, forVariable(variable), schema, "tbl_participant");
        addMetadata();
    }

    public CLTblParticipant(Path<? extends CLTblParticipant> path) {
        super(path.getType(), path.getMetadata(), "null", "tbl_participant");
        addMetadata();
    }

    public CLTblParticipant(PathMetadata metadata) {
        super(CLTblParticipant.class, metadata, "null", "tbl_participant");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(address, ColumnMetadata.named("address").withIndex(5).ofType(Types.VARCHAR).withSize(500));
        addMetadata(businessContactId, ColumnMetadata.named("business_contact_id").withIndex(9).ofType(Types.BIGINT).withSize(19));
        addMetadata(createdDate, ColumnMetadata.named("created_date").withIndex(7).ofType(Types.BIGINT).withSize(19));
        addMetadata(dfspCode, ColumnMetadata.named("dfsp_code").withIndex(2).ofType(Types.VARCHAR).withSize(100));
        addMetadata(dfspName, ColumnMetadata.named("dfsp_name").withIndex(4).ofType(Types.VARCHAR).withSize(100));
        addMetadata(mobile, ColumnMetadata.named("mobile").withIndex(6).ofType(Types.VARCHAR).withSize(20));
        addMetadata(name, ColumnMetadata.named("name").withIndex(3).ofType(Types.VARCHAR).withSize(100));
        addMetadata(participantId, ColumnMetadata.named("participant_id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(technicalContactId, ColumnMetadata.named("technical_contact_id").withIndex(10).ofType(Types.BIGINT).withSize(19));
        addMetadata(updatedDate, ColumnMetadata.named("updated_date").withIndex(8).ofType(Types.BIGINT).withSize(19));
    }

}

