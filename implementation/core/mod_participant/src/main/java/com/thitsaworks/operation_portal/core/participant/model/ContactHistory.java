package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.common.identifier.ContactHistoryId;
import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.common.type.Mobile;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_contact_history")
@Getter
@NoArgsConstructor
public class ContactHistory extends JpaEntity<ContactHistoryId> {

    @EmbeddedId
    protected ContactHistoryId contactHistoryId;


    @Embedded
    protected ContactId contactId;

    @Embedded
    protected ParticipantId participantId;

    @Column(name = "name")
    protected String name;

    @Column(name = "position")
    protected String position;

    @Column(name = "email")
    @Convert(converter = Email.JpaConverter.class)
    protected Email email;

    @Column(name = "mobile")
    @Convert(converter = Mobile.JpaConverter.class)
    protected Mobile mobile;

    @Column(name = "contact_type")
    @Enumerated(EnumType.STRING)
    protected ContactType contactType;

    public ContactHistory(ContactId contactId,
                          ParticipantId participantId,
                          String name,
                          String position,
                          Email email,
                          Mobile mobile,
                          ContactType contactType) {

        this.contactHistoryId = new ContactHistoryId(Snowflake.get().nextId());
        this.contactId(contactId);
        this.participantId(participantId);
        this.name(name);
        this.position(position);
        this.email(email);
        this.mobile(mobile);
        this.contactType(contactType);
    }

    @Override
    public ContactHistoryId getId() {

        return null;
    }

    public void contactId(ContactId contactId) {

        this.contactId = contactId;
    }

    public void participantId(ParticipantId participantId) {

        this.participantId = participantId;
    }

    public void name(String name) {

        this.name = name;
    }

    public void position(String position) {

        this.position = position;
    }

    public void email(Email email) {

        this.email = email;
    }

    public void mobile(Mobile mobile) {

        this.mobile = mobile;
    }

    public void contactType(ContactType contactType) {

        this.contactType = contactType;
    }

}
