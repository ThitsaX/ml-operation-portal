package com.thitsaworks.operation_portal.querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * DfspTblParticipantUser is a Querydsl query type for DfspTblParticipantUser
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class DfspTblParticipantUser extends com.querydsl.sql.RelationalPathBase<DfspTblParticipantUser> {

    private static final long serialVersionUID = -388505970;

    public static final DfspTblParticipantUser tblParticipantUser = new DfspTblParticipantUser("tbl_participant_user");

    public final NumberPath<Long> createdDate = createNumber("createdDate", Long.class);

    public final StringPath email = createString("email");

    public final StringPath firstName = createString("firstName");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath jobTitle = createString("jobTitle");

    public final StringPath lastName = createString("lastName");

    public final StringPath name = createString("name");

    public final NumberPath<Long> participantId = createNumber("participantId", Long.class);

    public final NumberPath<Long> updatedDate = createNumber("updatedDate", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final com.querydsl.sql.PrimaryKey<DfspTblParticipantUser> primary = createPrimaryKey(userId);

    public DfspTblParticipantUser(String variable) {
        super(DfspTblParticipantUser.class, forVariable(variable), "null", "tbl_participant_user");
        addMetadata();
    }

    public DfspTblParticipantUser(String variable, String schema, String table) {
        super(DfspTblParticipantUser.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public DfspTblParticipantUser(String variable, String schema) {
        super(DfspTblParticipantUser.class, forVariable(variable), schema, "tbl_participant_user");
        addMetadata();
    }

    public DfspTblParticipantUser(Path<? extends DfspTblParticipantUser> path) {
        super(path.getType(), path.getMetadata(), "null", "tbl_participant_user");
        addMetadata();
    }

    public DfspTblParticipantUser(PathMetadata metadata) {
        super(DfspTblParticipantUser.class, metadata, "null", "tbl_participant_user");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("created_date").withIndex(8).ofType(Types.BIGINT).withSize(19));
        addMetadata(email, ColumnMetadata.named("email").withIndex(4).ofType(Types.VARCHAR).withSize(100));
        addMetadata(firstName, ColumnMetadata.named("first_name").withIndex(5).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(isDeleted, ColumnMetadata.named("is_deleted").withIndex(10).ofType(Types.BIT).withSize(1));
        addMetadata(jobTitle, ColumnMetadata.named("job_title").withIndex(7).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(lastName, ColumnMetadata.named("last_name").withIndex(6).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(name, ColumnMetadata.named("name").withIndex(3).ofType(Types.VARCHAR).withSize(100));
        addMetadata(participantId, ColumnMetadata.named("participant_id").withIndex(2).ofType(Types.BIGINT).withSize(19));
        addMetadata(updatedDate, ColumnMetadata.named("updated_date").withIndex(9).ofType(Types.BIGINT).withSize(19));
        addMetadata(userId, ColumnMetadata.named("user_id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

