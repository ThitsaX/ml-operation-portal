package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.common.type.Mobile;
import com.thitsaworks.operation_portal.component.common.type.ParticipantName;
import com.thitsaworks.operation_portal.component.common.type.ParticipantStatus;
import com.thitsaworks.operation_portal.component.misc.exception.InputException;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@EntityListeners(value = {ParticipantCache.Updater.class})
@Table(name = "tbl_participant")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Participant extends JpaEntity<ParticipantId> {

    @EmbeddedId
    protected ParticipantId participantId;

    @Column(name = "participant_name")
    @Convert(converter = ParticipantName.JpaConverter.class)
    protected ParticipantName participantName;

    @Column(name = "description")
    protected String description;

    @Column(name = "address")
    protected String address;

    @Column(name = "mobile")
    @Convert(converter = Mobile.JpaConverter.class)
    protected Mobile mobile;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    protected ParticipantStatus participantStatus;

    @Column(name = "logo_file_type")
    protected String logoFiletype;

    @Lob
    @Column(
        name = "logo",
        columnDefinition = "LONGBLOB")
    protected byte[] logoBase64;

    @OneToMany(
        cascade = {CascadeType.ALL},
        mappedBy = "participant",
        orphanRemoval = true,
        fetch = FetchType.LAZY)
    protected Set<Contact> contacts = new HashSet<>();

    @OneToMany(
        cascade = {CascadeType.ALL},
        mappedBy = "participant",
        orphanRemoval = true,
        fetch = FetchType.EAGER)
    protected Set<User> users = new HashSet<>();

    @OneToMany(
        cascade = {CascadeType.ALL},
        mappedBy = "participant",
        orphanRemoval = true,
        fetch = FetchType.LAZY)
    @Getter(AccessLevel.NONE)
    protected Set<LiquidityProfile> liquidityProfiles = new HashSet<>();

    public Participant(ParticipantName participantName,
                       String description,
                       String address,
                       Mobile mobile,
                       ParticipantStatus participantStatus) {

        Validate.notNull(participantName);

        this.participantId = new ParticipantId(Snowflake.get()
                                                        .nextId());
        this.participantName = participantName;
        this.description = description;
        this.address = address;
        this.mobile = mobile;
        this.participantStatus = participantStatus;
    }

    public Contact addContact(String name, String title, Email email, Mobile mobile, ContactType contactType)
        throws ParticipantException {

        Contact contact = new Contact(name, title, email, mobile, contactType, this);

        boolean contactExists = this.contacts.stream()
                                             .anyMatch(c -> c.contactType.equals(contactType));

        if (contactExists) {
            throw new ParticipantException(
                ParticipantErrors.CONTACT_TYPE_ALREADY_REGISTERED.format(contactType.name()));
        }

        this.contacts.add(contact);

        return contact;
    }

    public Contact updateContact(ContactId contactId,
                                 String name,
                                 String title,
                                 Email email,
                                 Mobile mobile,
                                 ContactType contactType) throws ParticipantException {

        Optional<Contact> existingContact = this.contacts.stream()
                                                         .filter(c -> c.contactId.equals(contactId))
                                                         .findFirst();

        if (existingContact.isPresent()) {

            Contact contact = existingContact.get();

            boolean isChangingType = !contact.contactType.equals(contactType);
            boolean typeExist = this.contacts.stream()
                                             .anyMatch(c -> c != contact &&
                                                                c.contactType.equals(contactType));

            if (isChangingType && typeExist) {
                throw new ParticipantException(
                    ParticipantErrors.CONTACT_TYPE_ALREADY_REGISTERED.format(contactType.name()));
            }

            contact.name(name);
            contact.position(title);
            contact.email(email);
            contact.mobile(mobile);
            contact.contactType(contactType);

            return contact;

        } else {

            throw new InputException(ParticipantErrors.CONTACT_NOT_FOUND.format(contactId.getId()
                                                                                         .toString()));

        }
    }

    @Override
    public ParticipantId getId() {

        return this.participantId;
    }

    public User addUser(String name, Email email, String firstName, String lastName, String jobTitle) {

        Validate.notBlank(name);
        Validate.notNull(email);
        Validate.notNull(firstName);
        Validate.notNull(lastName);
        Validate.notNull(jobTitle);

        User user = new User(name, email, this, firstName, lastName, jobTitle);
        this.users.add(user);

        return user;
    }

    public LiquidityProfile addLiquidityProfile(String bankName,
                                                String accountName,
                                                String accountNumber,
                                                String currency,
                                                Boolean isActive)
        throws IllegalArgumentException, ParticipantException {

        Validate.notBlank(accountName);
        Validate.notBlank(accountNumber);
        Validate.notBlank(currency);

        LiquidityProfile liquidityProfile = new LiquidityProfile(this,
                                                                 bankName,
                                                                 accountName,
                                                                 accountNumber,
                                                                 currency,
                                                                 isActive);
        boolean currencyExist = this.liquidityProfiles.stream()
                                                      .anyMatch(profile -> profile.currency.equals(currency) &&
                                                                               profile.isActive);

        if (currencyExist) {
            throw new ParticipantException(ParticipantErrors.LIQUIDITY_PROFILE_ALREADY_REGISTERED.format(currency));
        }

        this.liquidityProfiles.add(liquidityProfile);

        return liquidityProfile;
    }

    public LiquidityProfile updateLiquidityProfile(LiquidityProfileId liquidityProfileId,
                                                   String bankName,
                                                   String accountName,
                                                   String accountNumber,
                                                   String currency) throws ParticipantException {

        Validate.notBlank(bankName);
        Validate.notBlank(accountName);
        Validate.notBlank(accountNumber);
        Validate.notBlank(currency);

        Optional<LiquidityProfile> existingLiquidityProfile = this.liquidityProfiles.stream()
                                                                                    .filter(profile -> profile.getLiquidityProfileId()
                                                                                                              .equals(
                                                                                                                  liquidityProfileId))
                                                                                    .findFirst();

        if (existingLiquidityProfile.isPresent()) {

            var liquidityProfile = existingLiquidityProfile.get();

            boolean isChangingCurrency = !liquidityProfile.currency.equals(currency);
            boolean currencyExist = this.liquidityProfiles.stream()
                                                          .anyMatch(profile -> profile != liquidityProfile &&
                                                                                   (profile.currency.equals(currency) &&
                                                                                        profile.isActive));

            if (isChangingCurrency && currencyExist) {
                throw new ParticipantException(ParticipantErrors.LIQUIDITY_PROFILE_ALREADY_REGISTERED.format(currency));
            }

            liquidityProfile.bankName(bankName);
            liquidityProfile.accountName(accountName);
            liquidityProfile.accountNumber(accountNumber);
            liquidityProfile.currency(currency);

            return liquidityProfile;

        } else {

            throw new InputException(
                ParticipantErrors.LIQUIDITY_PROFILE_NOT_FOUND.format(liquidityProfileId.getId()
                                                                                       .toString()));

        }

    }

    public boolean removeLiquidityProfile(LiquidityProfileId liquidityProfileId) {

        Validate.notNull(liquidityProfileId);

        var
            optLiquidityProfile =
            this.liquidityProfiles.stream()
                                  .filter(item -> item.liquidityProfileId.equals(liquidityProfileId))
                                  .findFirst();

        if (optLiquidityProfile.isPresent()) {

            LiquidityProfile liquidityProfile = optLiquidityProfile.get();
            liquidityProfile.isActive(false);

            return true;
        }

        return false;

    }

    public boolean removeContact(ContactId contactId) {

        Validate.notNull(contactId);

        var
            optContact =
            this.contacts.stream()
                         .filter(item -> item.contactId.equals(contactId))
                         .findFirst();

        return optContact.filter(contact -> this.contacts.remove(contact))
                         .isPresent();
    }

    public Participant description(String description) {

        this.description = description;
        return this;

    }

    public Participant participantName(ParticipantName participantName) {

        this.participantName = participantName;
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

    public Participant status(ParticipantStatus participantStatus) {

        this.participantStatus = participantStatus;
        return this;

    }

    public Participant logoFileType(String logoFiletype) {

        this.logoFiletype = logoFiletype;
        return this;
    }

    public Participant logoBase64(byte[] logoBase64) {

        this.logoBase64 = logoBase64;
        return this;
    }

}
