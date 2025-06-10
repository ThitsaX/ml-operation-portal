package com.thitsaworks.operation_portal.report_querydsl.dbtable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * CLParticipantContact is a Querydsl query type for CLParticipantContact
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class CLParticipantContact extends com.querydsl.sql.RelationalPathBase<CLParticipantContact> {

    private static final long serialVersionUID = 445674858;

    public static final CLParticipantContact participantContact = new CLParticipantContact("participantContact");

    public final NumberPath<Integer> contactTypeId = createNumber("contactTypeId", Integer.class);

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.sql.Timestamp> createdDate = createDateTime("createdDate", java.sql.Timestamp.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final NumberPath<Integer> participantContactId = createNumber("participantContactId", Integer.class);

    public final NumberPath<Integer> participantId = createNumber("participantId", Integer.class);

    public final NumberPath<Integer> priorityPreference = createNumber("priorityPreference", Integer.class);

    public final StringPath value = createString("value");

    public final com.querydsl.sql.PrimaryKey<CLParticipantContact> primary = createPrimaryKey(participantContactId);

    public final com.querydsl.sql.ForeignKey<CLContactType> participantcontactContacttypeidForeign = createForeignKey(contactTypeId, "contactTypeId");

    public final com.querydsl.sql.ForeignKey<CLParticipant> participantcontactParticipantidForeign = createForeignKey(participantId, "participantId");

    public CLParticipantContact(String variable) {
        super(CLParticipantContact.class, forVariable(variable), "null", "participantContact");
        addMetadata();
    }

    public CLParticipantContact(String variable, String schema, String table) {
        super(CLParticipantContact.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public CLParticipantContact(String variable, String schema) {
        super(CLParticipantContact.class, forVariable(variable), schema, "participantContact");
        addMetadata();
    }

    public CLParticipantContact(Path<? extends CLParticipantContact> path) {
        super(path.getType(), path.getMetadata(), "null", "participantContact");
        addMetadata();
    }

    public CLParticipantContact(PathMetadata metadata) {
        super(CLParticipantContact.class, metadata, "null", "participantContact");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(contactTypeId, ColumnMetadata.named("contactTypeId").withIndex(3).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(createdBy, ColumnMetadata.named("createdBy").withIndex(8).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(createdDate, ColumnMetadata.named("createdDate").withIndex(7).ofType(Types.TIMESTAMP).withSize(19).notNull());
        addMetadata(isActive, ColumnMetadata.named("isActive").withIndex(6).ofType(Types.BIT).withSize(1).notNull());
        addMetadata(participantContactId, ColumnMetadata.named("participantContactId").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(participantId, ColumnMetadata.named("participantId").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(priorityPreference, ColumnMetadata.named("priorityPreference").withIndex(5).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(value, ColumnMetadata.named("value").withIndex(4).ofType(Types.VARCHAR).withSize(256).notNull());
    }

}

