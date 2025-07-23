package com.thitsaworks.operation_portal.core.hubuser.model;

import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_hub_user")
@Getter
@NoArgsConstructor
public class HubUser extends JpaEntity<HubUserId> {

    @EmbeddedId
    protected HubUserId userId;

    @Column(name = "name")
    protected String name;

    @Column(name = "email")
    @Convert(converter = Email.JpaConverter.class)
    protected Email email;

    @Column(name = "first_name")
    protected String firstName;

    @Column(name = "last_name")
    protected String lastName;

    @Column(name = "job_title")
    protected String jobTitle;

    @Column(name = "is_deleted")
    protected boolean isDeleted;

    public HubUser(String name, Email email, String firstName, String lastName, String jobTitle) {

        Validate.notBlank(name);
        Validate.notNull(email);
        Validate.notNull(firstName);
        Validate.notNull(lastName);
        Validate.notNull(jobTitle);

        this.userId = new HubUserId(Snowflake.get().nextId());
        this.name = name;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;

    }

    @Override
    public HubUserId getId() {

        return this.userId;
    }

    public HubUser name(String name) {

        this.name = name;
        return this;

    }

    public HubUser firstName(String firstName) {

        this.firstName = firstName;
        return this;

    }

    public HubUser lastName(String lastName) {

        this.lastName = lastName;
        return this;

    }

    public HubUser jobTitle(String jobTitle) {

        this.jobTitle = jobTitle;
        return this;

    }

    public HubUser isDeleted(boolean isDeleted) {

        this.isDeleted = isDeleted;
        return this;

    }

}


