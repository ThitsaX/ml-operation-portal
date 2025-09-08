package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.participant.cache.UserCache;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

@Entity
@EntityListeners(value = {UserCache.Updater.class})
@Table(name = "tbl_user")
@Getter
@NoArgsConstructor
public class User extends JpaEntity<UserId> {

    @EmbeddedId
    protected UserId userId;

    @ManyToOne()
    @JoinColumn(name = "participant_id")
    protected Participant participant;

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

    public User(String name, Email email, Participant participant, String firstName, String lastName,
                String jobTitle) {

        Validate.notNull(participant);

        this.userId = new UserId(Snowflake.get().nextId());
        this.name = name;
        this.email = email;
        this.participant = participant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
    }

    @Override
    public UserId getId() {

        return this.userId;
    }

    public User name(String name) {

        this.name = name;
        return this;

    }

    public User firstName(String firstName) {

        this.firstName = firstName;
        return this;

    }

    public User lastName(String lastName) {

        this.lastName = lastName;
        return this;

    }

    public User jobTitle(String jobTitle) {

        this.jobTitle = jobTitle;
        return this;

    }

    public User isDeleted(boolean isDeleted) {

        this.isDeleted = isDeleted;
        return this;

    }

}


