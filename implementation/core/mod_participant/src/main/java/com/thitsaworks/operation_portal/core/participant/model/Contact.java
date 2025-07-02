package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.component.util.Snowflake;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_contact")
@Getter
@NoArgsConstructor
public class Contact extends JpaEntity<ContactId> {

    @EmbeddedId
    protected ContactId contactId;

    @ManyToOne()
    @JoinColumn(name = "participant_id")
    protected Participant participant;

    @Column(name = "name")
    protected String name;

    @Column(name = "title")
    protected String title;

    @Column(name = "email")
    @Convert(converter = Email.JpaConverter.class)
    protected Email email;

    @Column(name = "mobile")
    @Convert(converter = Mobile.JpaConverter.class)
    protected Mobile mobile;

    @Column(name = "contact_type")
    @Enumerated(EnumType.STRING)
    protected ContactType contactType;

    public Contact(String name, String title, Email email, Mobile mobile, ContactType contactType,
                   Participant participant) {

        this.contactId = new ContactId(Snowflake.get().nextId());
        this.name = name;
        this.title = title;
        this.email = email;
        this.mobile = mobile;
        this.contactType = contactType;
        this.participant = participant;
    }

    @Override
    public ContactId getId() {

        return this.contactId;
    }

    public Contact name(String name) {

        this.name = name;
        return this;

    }

    public Contact title(String title) {

        this.title = title;
        return this;

    }

    public Contact email(Email email) {

        this.email = email;
        return this;

    }

    public Contact mobile(Mobile mobile) {

        this.mobile = mobile;
        return this;

    }

    public Contact contactType(ContactType contactType) {

        this.contactType = contactType;
        return this;

    }

}
