package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
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
@EntityListeners(value = {ParticipantUserCache.Updater.class})
@Table(name = "tbl_participant_user")
@Getter
@NoArgsConstructor
public class ParticipantUser extends JpaEntity<ParticipantUserId> {

    @EmbeddedId
    protected ParticipantUserId participantUserId;

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

    public ParticipantUser(String name, Email email, Participant participant, String firstName, String lastName,
                           String jobTitle) {

        Validate.notNull(participant);

        this.participantUserId = new ParticipantUserId(Snowflake.get().nextId());
        this.name = name;
        this.email = email;
        this.participant = participant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
    }

    @Override
    public ParticipantUserId getId() {

        return this.participantUserId;
    }

    public ParticipantUser name(String name) {

        this.name = name;
        return this;

    }

    public ParticipantUser firstName(String firstName) {

        this.firstName = firstName;
        return this;

    }

    public ParticipantUser lastName(String lastName) {

        this.lastName = lastName;
        return this;

    }

    public ParticipantUser jobTitle(String jobTitle) {

        this.jobTitle = jobTitle;
        return this;

    }

    public ParticipantUser isDeleted(boolean isDeleted) {

        this.isDeleted = isDeleted;
        return this;

    }

}


