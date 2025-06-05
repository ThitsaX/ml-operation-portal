package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLTblPrincipal is a Querydsl query type for CLTblPrincipal
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLTblPrincipal extends com.querydsl.sql.RelationalPathBase<CLTblPrincipal> {

    private static final long serialVersionUID = 1523728333;

    public static final CLTblPrincipal tblPrincipal = new CLTblPrincipal("tbl_principal");

    public final NumberPath<Long> accessKey = createNumber("accessKey", Long.class);

    public final NumberPath<Long> createdDate = createNumber("createdDate", Long.class);

    public final NumberPath<Long> principalId = createNumber("principalId", Long.class);

    public final StringPath realm = createString("realm");

    public final NumberPath<Long> realmId = createNumber("realmId", Long.class);

    public final StringPath secretKey = createString("secretKey");

    public final StringPath sha256PasswordHex = createString("sha256PasswordHex");

    public final StringPath status = createString("status");

    public final NumberPath<Long> updatedDate = createNumber("updatedDate", Long.class);

    public final StringPath userRoleType = createString("userRoleType");

    public final com.querydsl.sql.PrimaryKey<CLTblPrincipal> primary = createPrimaryKey(principalId);

    public CLTblPrincipal(String variable) {
        super(CLTblPrincipal.class, forVariable(variable), "null", "tbl_principal");
        addMetadata();
    }

    public CLTblPrincipal(String variable, String schema, String table) {
        super(CLTblPrincipal.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLTblPrincipal(String variable, String schema) {
        super(CLTblPrincipal.class, forVariable(variable), schema, "tbl_principal");
        addMetadata();
    }

    public CLTblPrincipal(Path<? extends CLTblPrincipal> path) {
        super(path.getType(), path.getMetadata(), "null", "tbl_principal");
        addMetadata();
    }

    public CLTblPrincipal(PathMetadata metadata) {
        super(CLTblPrincipal.class, metadata, "null", "tbl_principal");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(accessKey, ColumnMetadata.named("access_key").withIndex(2).ofType(Types.BIGINT).withSize(19));
        addMetadata(createdDate, ColumnMetadata.named("created_date").withIndex(8).ofType(Types.BIGINT).withSize(19));
        addMetadata(principalId, ColumnMetadata.named("principal_id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(realm, ColumnMetadata.named("realm").withIndex(4).ofType(Types.VARCHAR).withSize(20));
        addMetadata(realmId, ColumnMetadata.named("realm_id").withIndex(5).ofType(Types.BIGINT).withSize(19));
        addMetadata(secretKey, ColumnMetadata.named("secret_key").withIndex(3).ofType(Types.VARCHAR).withSize(64));
        addMetadata(sha256PasswordHex, ColumnMetadata.named("sha_256_password_hex").withIndex(6).ofType(Types.VARCHAR).withSize(64));
        addMetadata(status, ColumnMetadata.named("status").withIndex(7).ofType(Types.VARCHAR).withSize(10));
        addMetadata(updatedDate, ColumnMetadata.named("updated_date").withIndex(9).ofType(Types.BIGINT).withSize(19));
        addMetadata(userRoleType, ColumnMetadata.named("user_role_type").withIndex(10).ofType(Types.VARCHAR).withSize(100));
    }

}

