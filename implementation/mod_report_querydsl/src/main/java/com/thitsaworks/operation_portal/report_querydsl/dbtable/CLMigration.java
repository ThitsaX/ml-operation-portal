package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLMigration is a Querydsl query type for CLMigration
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLMigration extends com.querydsl.sql.RelationalPathBase<CLMigration> {

    private static final long serialVersionUID = -996721871;

    public static final CLMigration migration = new CLMigration("migration");

    public final NumberPath<Integer> batch = createNumber("batch", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.sql.Timestamp> migrationTime = createDateTime("migrationTime", java.sql.Timestamp.class);

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<CLMigration> primary = createPrimaryKey(id);

    public CLMigration(String variable) {
        super(CLMigration.class, forVariable(variable), "null", "migration");
        addMetadata();
    }

    public CLMigration(String variable, String schema, String table) {
        super(CLMigration.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLMigration(String variable, String schema) {
        super(CLMigration.class, forVariable(variable), schema, "migration");
        addMetadata();
    }

    public CLMigration(Path<? extends CLMigration> path) {
        super(path.getType(), path.getMetadata(), "null", "migration");
        addMetadata();
    }

    public CLMigration(PathMetadata metadata) {
        super(CLMigration.class, metadata, "null", "migration");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(batch, ColumnMetadata.named("batch").withIndex(3).ofType(Types.INTEGER).withSize(10));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(migrationTime, ColumnMetadata.named("migration_time").withIndex(4).ofType(Types.TIMESTAMP).withSize(19));
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(255));
    }

}

