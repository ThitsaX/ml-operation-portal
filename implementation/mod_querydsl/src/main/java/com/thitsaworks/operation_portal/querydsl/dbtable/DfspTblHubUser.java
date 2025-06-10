package com.thitsaworks.operation_portal.querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * DfspTblHubUser is a Querydsl query type for DfspTblHubUser
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class DfspTblHubUser extends com.querydsl.sql.RelationalPathBase<DfspTblHubUser> {

    private static final long serialVersionUID = 412533584;

    public static final DfspTblHubUser tblHubUser = new DfspTblHubUser("tbl_hub_user");

    public final NumberPath<Long> createdDate = createNumber("createdDate", Long.class);

    public final StringPath email = createString("email");

    public final StringPath firstName = createString("firstName");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath jobTitle = createString("jobTitle");

    public final StringPath lastName = createString("lastName");

    public final StringPath name = createString("name");

    public final NumberPath<Long> updatedDate = createNumber("updatedDate", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final com.querydsl.sql.PrimaryKey<DfspTblHubUser> primary = createPrimaryKey(userId);

    public DfspTblHubUser(String variable) {
        super(DfspTblHubUser.class, forVariable(variable), "null", "tbl_hub_user");
        addMetadata();
    }

    public DfspTblHubUser(String variable, String schema, String table) {
        super(DfspTblHubUser.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public DfspTblHubUser(String variable, String schema) {
        super(DfspTblHubUser.class, forVariable(variable), schema, "tbl_hub_user");
        addMetadata();
    }

    public DfspTblHubUser(Path<? extends DfspTblHubUser> path) {
        super(path.getType(), path.getMetadata(), "null", "tbl_hub_user");
        addMetadata();
    }

    public DfspTblHubUser(PathMetadata metadata) {
        super(DfspTblHubUser.class, metadata, "null", "tbl_hub_user");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createdDate, ColumnMetadata.named("created_date").withIndex(7).ofType(Types.BIGINT).withSize(19));
        addMetadata(email, ColumnMetadata.named("email").withIndex(3).ofType(Types.VARCHAR).withSize(100));
        addMetadata(firstName, ColumnMetadata.named("first_name").withIndex(4).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(isDeleted, ColumnMetadata.named("is_deleted").withIndex(9).ofType(Types.BIT).withSize(1));
        addMetadata(jobTitle, ColumnMetadata.named("job_title").withIndex(6).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(lastName, ColumnMetadata.named("last_name").withIndex(5).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(50));
        addMetadata(updatedDate, ColumnMetadata.named("updated_date").withIndex(8).ofType(Types.BIGINT).withSize(19));
        addMetadata(userId, ColumnMetadata.named("user_id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

