package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTblHubUser is a Querydsl query type for CLTblHubUser
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTblHubUser extends com.querydsl.sql.RelationalPathBase<CLTblHubUser> {

    private static final long serialVersionUID = -382886465;

    public static final CLTblHubUser tblHubUser = new CLTblHubUser("tbl_hub_user");

    public final NumberPath<Long> createdDate = createNumber("createdDate", Long.class);

    public final StringPath email = createString("email");

    public final StringPath firstName = createString("firstName");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath jobTitle = createString("jobTitle");

    public final StringPath lastName = createString("lastName");

    public final StringPath name = createString("name");

    public final NumberPath<Long> updatedDate = createNumber("updatedDate", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final com.querydsl.sql.PrimaryKey<CLTblHubUser> primary = createPrimaryKey(userId);

    public CLTblHubUser(String variable) {
        super(CLTblHubUser.class, forVariable(variable), "null", "tbl_hub_user");
        addMetadata();
    }

    public CLTblHubUser(String variable, String schema, String table) {
        super(CLTblHubUser.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTblHubUser(String variable, String schema) {
        super(CLTblHubUser.class, forVariable(variable), schema, "tbl_hub_user");
        addMetadata();
    }

    public CLTblHubUser(Path<? extends CLTblHubUser> path) {
        super(path.getType(), path.getMetadata(), "null", "tbl_hub_user");
        addMetadata();
    }

    public CLTblHubUser(PathMetadata metadata) {
        super(CLTblHubUser.class, metadata, "null", "tbl_hub_user");
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

