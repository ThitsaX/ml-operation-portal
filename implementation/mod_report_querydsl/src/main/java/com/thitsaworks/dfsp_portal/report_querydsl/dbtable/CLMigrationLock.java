package com.thitsaworks.dfsp_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLMigrationLock is a Querydsl query type for CLMigrationLock
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLMigrationLock extends com.querydsl.sql.RelationalPathBase<CLMigrationLock> {

    private static final long serialVersionUID = -480742404;

    public static final CLMigrationLock migrationLock = new CLMigrationLock("migration_lock");

    public final NumberPath<Integer> index = createNumber("index", Integer.class);

    public final NumberPath<Integer> isLocked = createNumber("isLocked", Integer.class);

    public final com.querydsl.sql.PrimaryKey<CLMigrationLock> primary = createPrimaryKey(index);

    public CLMigrationLock(String variable) {
        super(CLMigrationLock.class, forVariable(variable), "null", "migration_lock");
        addMetadata();
    }

    public CLMigrationLock(String variable, String schema, String table) {
        super(CLMigrationLock.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLMigrationLock(String variable, String schema) {
        super(CLMigrationLock.class, forVariable(variable), schema, "migration_lock");
        addMetadata();
    }

    public CLMigrationLock(Path<? extends CLMigrationLock> path) {
        super(path.getType(), path.getMetadata(), "null", "migration_lock");
        addMetadata();
    }

    public CLMigrationLock(PathMetadata metadata) {
        super(CLMigrationLock.class, metadata, "null", "migration_lock");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(index, ColumnMetadata.named("index").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(isLocked, ColumnMetadata.named("is_locked").withIndex(2).ofType(Types.INTEGER).withSize(10));
    }

}

