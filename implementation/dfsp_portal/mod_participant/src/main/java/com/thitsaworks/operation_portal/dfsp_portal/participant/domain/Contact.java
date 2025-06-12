package com.thitsaworks.operation_portal.dfsp_portal.participant.domain;

import com.thitsaworks.operation_portal.component.misc.data.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.component.util.Snowflake;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ContactId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_contact")
@Getter
@NoArgsConstructor
public class Contact extends JpaEntity<ContactId> {

    @EmbeddedId
    protected ContactId contactId;

    @OneToOne(cascade = {CascadeType.ALL})
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

    public Contact(String name, String title, Email email, Mobile mobile, Participant participant) {

        this.contactId = new ContactId(Snowflake.get().nextId());
        this.name = name;
        this.title = title;
        this.email = email;
        this.mobile = mobile;
        this.participant = participant;
    }

    @Override
    protected ContactId getPrimaryId() {

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

}
