package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ContactType;
import com.thitsaworks.operation_portal.component.common.type.DfspCode;
import com.thitsaworks.operation_portal.component.misc.exception.InputException;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

    @Column(name = "logo_data_type")
    protected String logoDatatype;

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
        fetch = FetchType.LAZY)
    protected Set<ParticipantUser> participantUsers = new HashSet<>();

    @OneToMany(
        cascade = {CascadeType.ALL},
        mappedBy = "participant",
        orphanRemoval = true,
        fetch = FetchType.LAZY)
    @Getter(AccessLevel.NONE)
    protected Set<LiquidityProfile> liquidityProfiles = new HashSet<>();

    public Participant(DfspCode dfspCode,
                       String name,
                       String dfspName,
                       String address,
                       Mobile mobile) {

        Validate.notNull(dfspCode);
        Validate.notBlank(name);
        Validate.notBlank(dfspName);

        this.participantId = new ParticipantId(Snowflake.get()
                                                        .nextId());
        this.dfspCode = dfspCode;
        this.name = name;
        this.dfspName = dfspName;
        this.address = address;
        this.mobile = mobile;
    }

    public Contact addContact(String name, String title, Email email, Mobile mobile, ContactType contactType) {

        Contact contact = new Contact(name, title, email, mobile, contactType, this);

        boolean contactExists = this.contacts.stream()
                                             .anyMatch(c -> c.contactType.equals(contactType));

        if (contactExists) {
            throw new InputException(ParticipantErrors.CONTACT_ALREADY_REGISTERED);
        }

        this.contacts.add(contact);

        return contact;
    }

    public Contact updateContact(ContactId contactId,
                                 String name,
                                 String title,
                                 Email email,
                                 Mobile mobile,
                                 ContactType contactType) {

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
                throw new InputException(ParticipantErrors.CONTACT_ALREADY_REGISTERED);
            }

            contact.name(name);
            contact.position(title);
            contact.email(email);
            contact.mobile(mobile);
            contact.contactType(contactType);

            return contact;

        } else {

            throw new InputException(ParticipantErrors.CONTACT_NOT_FOUND);

        }
    }

    @Override
    public ParticipantId getId() {

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

    public LiquidityProfile addLiquidityProfile(String bankName,
                                                String accountName,
                                                String accountNumber,
                                                String currency,
                                                Boolean isActive) throws IllegalArgumentException {

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
            throw new InputException(ParticipantErrors.LIQUIDITY_PROFILE_ALREADY_REGISTERED);
        }

        this.liquidityProfiles.add(liquidityProfile);

        return liquidityProfile;
    }

    public LiquidityProfile updateLiquidityProfile(LiquidityProfileId liquidityProfileId,
                                                   String bankName,
                                                   String accountName,
                                                   String accountNumber,
                                                   String currency) {

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
                throw new InputException(ParticipantErrors.LIQUIDITY_PROFILE_ALREADY_REGISTERED);
            }

            liquidityProfile.bankName(bankName);
            liquidityProfile.accountName(accountName);
            liquidityProfile.accountNumber(accountNumber);
            liquidityProfile.currency(currency);

            return liquidityProfile;

        } else {

            throw new InputException(ParticipantErrors.LIQUIDITY_PROFILE_NOT_FOUND);

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

    public Participant logoDataType(String logoDatatype){

        this.logoDatatype = logoDatatype;
        return this;
    }

    public Participant logoBase64(byte[] logoBase64) {

        this.logoBase64 = logoBase64;
        return this;
    }

}
