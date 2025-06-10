package com.thitsaworks.operation_portal.participant.domain;

import com.thitsaworks.operation_portal.component.data.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.component.util.Snowflake;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.query.cache.hazelcast.HazelcastParticipantCache;
import com.thitsaworks.operation_portal.participant.type.DfspCode;
import lombok.AccessLevel;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(value = {HazelcastParticipantCache.Updater.class})
@Table(name = "tbl_participant")
@Getter
@NoArgsConstructor
public class Participant extends JpaEntity<ParticipantId> {

    @EmbeddedId
    protected ParticipantId participantId;

    @Column(name = "name")
    protected String name;

    @Column(name = "dfsp_code")
    @Convert(converter = DfspCode.JpaConverter.class)
    protected DfspCode dfspCode;

    @Column(name = "dfsp_name")
    protected String dfspName;

    @Column(name = "address")
    protected String address;

    @Column(name = "mobile")
    @Convert(converter = Mobile.JpaConverter.class)
    protected Mobile mobile;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "business_contact_id")
    protected Contact businessContact;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "technical_contact_id")
    protected Contact technicalContact;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "participant", orphanRemoval = true, fetch = FetchType.LAZY)
    @Getter(AccessLevel.NONE)
    protected Set<ParticipantUser> participantUsers = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "participant", orphanRemoval = true, fetch = FetchType.LAZY)
    @Getter(AccessLevel.NONE)
    protected Set<LiquidityProfile> liquidityProfiles = new HashSet<>();

    public Participant(DfspCode dfspCode, String name, String dfspName, String address, Mobile mobile) {

        Validate.notNull(dfspCode);
        Validate.notBlank(name);
        Validate.notBlank(dfspName);
        Validate.notBlank(address);
        Validate.notNull(mobile);

        this.participantId = new ParticipantId(Snowflake.get().nextId());
        this.dfspCode = dfspCode;
        this.name = name;
        this.dfspName=dfspName;
        this.address = address;
        this.mobile = mobile;
    }

    public void businessContact(String name, String title, Email email, Mobile mobile) {

        this.businessContact = new Contact(name, title, email, mobile, this);
    }

    public void technicalContact(String name, String title, Email email, Mobile mobile) {

        this.technicalContact = new Contact(name, title, email, mobile, this);
    }

    @Override
    protected ParticipantId getPrimaryId() {

        return this.participantId;
    }

    public ParticipantUser addUser(String name, Email email, String firstName, String lastName, String jobTitle) {

        Validate.notBlank(name);
        Validate.notNull(email);
        Validate.notNull(firstName);
        Validate.notNull(lastName);
        Validate.notNull(jobTitle);

        ParticipantUser participantUser = new ParticipantUser(name, email, this, firstName, lastName, jobTitle);
        this.participantUsers.add(participantUser);

        return participantUser;
    }

    public LiquidityProfile addLiquidityProfile(String accountName, String accountNumber, String currency,
                                                Boolean isActive) throws IllegalArgumentException {

        Validate.notBlank(accountName);
        Validate.notBlank(accountNumber);
        Validate.notBlank(currency);

        LiquidityProfile liquidityProfile = new LiquidityProfile(this, accountName, accountNumber, currency, isActive);
        this.liquidityProfiles.add(liquidityProfile);

        return liquidityProfile;
    }

    public boolean removeLiquidityProfile(LiquidityProfile liquidityProfile) {

        Validate.notNull(liquidityProfile);

        return this.liquidityProfiles.remove(liquidityProfile);
    }

    public Participant name(String name) {

        this.name = name;
        return this;

    }

    public Participant dfsp_name(String dfspName) {

        this.dfspName = dfspName;
        return this;

    }

    public Participant dfspCode(DfspCode dfspCode) {

        this.dfspCode = dfspCode;
        return this;

    }

    public Participant address(String address) {

        this.address = address;
        return this;

    }

    public Participant mobile(Mobile mobile) {

        this.mobile = mobile;
        return this;

    }

}
