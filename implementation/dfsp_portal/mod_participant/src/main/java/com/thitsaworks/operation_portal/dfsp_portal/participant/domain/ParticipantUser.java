package com.thitsaworks.operation_portal.dfsp_portal.participant.domain;

import com.thitsaworks.operation_portal.component.misc.data.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.util.Snowflake;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.operation_portal.dfsp_portal.participant.query.cache.hazelcast.HazelcastParticipantUserCache;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@EntityListeners(value = {HazelcastParticipantUserCache.Updater.class})
@Table(name = "tbl_participant_user")
@Getter
@NoArgsConstructor
public class ParticipantUser extends JpaEntity<ParticipantUserId> {

    @EmbeddedId
    protected ParticipantUserId participantUserId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
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
    protected ParticipantUserId getPrimaryId() {

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


