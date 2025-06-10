package com.thitsaworks.operation_portal.querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * DfspTblContact is a Querydsl query type for DfspTblContact
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class DfspTblContact extends com.querydsl.sql.RelationalPathBase<DfspTblContact> {

    private static final long serialVersionUID = 110195984;

    public static final DfspTblContact tblContact = new DfspTblContact("tbl_contact");

    public final NumberPath<Long> contactId = createNumber("contactId", Long.class);

    public final NumberPath<Long> createdDate = createNumber("createdDate", Long.class);

    public final StringPath email = createString("email");

    public final StringPath mobile = createString("mobile");

    public final StringPath name = createString("name");

    public final NumberPath<Long> participantId = createNumber("participantId", Long.class);

    public final StringPath title = createString("title");

    public final NumberPath<Long> updatedDate = createNumber("updatedDate", Long.class);

    public final com.querydsl.sql.PrimaryKey<DfspTblContact> primary = createPrimaryKey(contactId);

    public DfspTblContact(String variable) {
        super(DfspTblContact.class, forVariable(variable), "null", "tbl_contact");
        addMetadata();
    }

    public DfspTblContact(String variable, String schema, String table) {
        super(DfspTblContact.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public DfspTblContact(String variable, String schema) {
        super(DfspTblContact.class, forVariable(variable), schema, "tbl_contact");
        addMetadata();
    }

    public DfspTblContact(Path<? extends DfspTblContact> path) {
        super(path.getType(), path.getMetadata(), "null", "tbl_contact");
        addMetadata();
    }

    public DfspTblContact(PathMetadata metadata) {
        super(DfspTblContact.class, metadata, "null", "tbl_contact");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(contactId, ColumnMetadata.named("contact_id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(createdDate, ColumnMetadata.named("created_date").withIndex(7).ofType(Types.BIGINT).withSize(19));
        addMetadata(email, ColumnMetadata.named("email").withIndex(5).ofType(Types.VARCHAR).withSize(100));
        addMetadata(mobile, ColumnMetadata.named("mobile").withIndex(6).ofType(Types.VARCHAR).withSize(1000));
        addMetadata(name, ColumnMetadata.named("name").withIndex(3).ofType(Types.VARCHAR).withSize(200));
        addMetadata(participantId, ColumnMetadata.named("participant_id").withIndex(2).ofType(Types.BIGINT).withSize(19));
        addMetadata(title, ColumnMetadata.named("title").withIndex(4).ofType(Types.VARCHAR).withSize(100));
        addMetadata(updatedDate, ColumnMetadata.named("updated_date").withIndex(8).ofType(Types.BIGINT).withSize(19));
    }

}

