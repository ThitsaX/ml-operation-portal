package com.thitsaworks.operation_portal.querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * DfspTblAnnouncement is a Querydsl query type for DfspTblAnnouncement
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class DfspTblAnnouncement extends com.querydsl.sql.RelationalPathBase<DfspTblAnnouncement> {

    private static final long serialVersionUID = -251981225;

    public static final DfspTblAnnouncement tblAnnouncement = new DfspTblAnnouncement("tbl_announcement");

    public final NumberPath<Long> announcementDate = createNumber("announcementDate", Long.class);

    public final StringPath announcementDetail = createString("announcementDetail");

    public final NumberPath<Long> announcementId = createNumber("announcementId", Long.class);

    public final StringPath announcementTitle = createString("announcementTitle");

    public final NumberPath<Long> createdDate = createNumber("createdDate", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Long> updatedDate = createNumber("updatedDate", Long.class);

    public final com.querydsl.sql.PrimaryKey<DfspTblAnnouncement> primary = createPrimaryKey(announcementId);

    public DfspTblAnnouncement(String variable) {
        super(DfspTblAnnouncement.class, forVariable(variable), "null", "tbl_announcement");
        addMetadata();
    }

    public DfspTblAnnouncement(String variable, String schema, String table) {
        super(DfspTblAnnouncement.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public DfspTblAnnouncement(String variable, String schema) {
        super(DfspTblAnnouncement.class, forVariable(variable), schema, "tbl_announcement");
        addMetadata();
    }

    public DfspTblAnnouncement(Path<? extends DfspTblAnnouncement> path) {
        super(path.getType(), path.getMetadata(), "null", "tbl_announcement");
        addMetadata();
    }

    public DfspTblAnnouncement(PathMetadata metadata) {
        super(DfspTblAnnouncement.class, metadata, "null", "tbl_announcement");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(announcementDate, ColumnMetadata.named("announcement_date").withIndex(3).ofType(Types.BIGINT).withSize(19));
        addMetadata(announcementDetail, ColumnMetadata.named("announcement_detail").withIndex(6).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(announcementId, ColumnMetadata.named("announcement_id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(announcementTitle, ColumnMetadata.named("announcement_title").withIndex(2).ofType(Types.LONGVARCHAR).withSize(65535));
        addMetadata(createdDate, ColumnMetadata.named("created_date").withIndex(4).ofType(Types.BIGINT).withSize(19));
        addMetadata(isDeleted, ColumnMetadata.named("is_deleted").withIndex(7).ofType(Types.BIT).withSize(1));
        addMetadata(updatedDate, ColumnMetadata.named("updated_date").withIndex(5).ofType(Types.BIGINT).withSize(19));
    }

}

