package com.thitsaworks.operation_portal.core.iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.security.OperationPortalCrypto;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.exception.IAMIgnorableException;
import com.thitsaworks.operation_portal.core.iam.listener.PrincipalListener;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@EntityListeners(value = {PrincipalCache.Updater.class, PrincipalListener.class})
@Table(name = "tbl_principal")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Principal extends JpaEntity<AccessKey> {

    @EmbeddedId
    protected PrincipalId principalId;

    @Embedded
    protected AccessKey accessKey;

    @Column(name = "secret_key")
    protected String secretKey;

    @Column(name = "realm")
    @Enumerated(EnumType.STRING)
    protected RealmType realmType;

    @Embedded
    protected RealmId realmId;

    @Getter(AccessLevel.NONE)
    @Column(name = "sha_256_password_hex")
    protected String sha256PasswordHex;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    protected PrincipalStatus principalStatus;

    @Getter(AccessLevel.NONE)
    @OneToMany(
        mappedBy = "principal",
        cascade = ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER)
    protected Set<PrincipalRole> roles = new HashSet<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(
        mappedBy = "principal",
        cascade = ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER)
    protected Set<PrincipalGrant> grants = new HashSet<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(
        mappedBy = "principal",
        cascade = ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER)
    protected Set<BlockedAction> denials = new HashSet<>();

    @Override
    public AccessKey getId() {

        return this.accessKey;
    }

    public Principal(PrincipalId principalId,
                     RealmType realmType,
                     String sha256PasswordHex,
                     RealmId realmId,
                     PrincipalStatus principalStatus) {

        this.principalId = principalId;
        this.accessKey = new AccessKey(Snowflake.get()
                                                .nextId());
        this.secretKey =
            UUID.randomUUID()
                .toString();
        this.realmType = realmType;
        this.realmId = realmId;
        this.sha256PasswordHex = OperationPortalCrypto.sha256Hex(sha256PasswordHex);
        this.principalStatus = principalStatus;

    }

    public PrincipalRole assignRole(Role role) throws IAMException {

        if (this.roles.stream()
                      .anyMatch(existing -> existing.role.equals(role))) {

            throw new IAMException(IAMErrors.ROLE_ALREADY_ASSIGN_TO_USER);
        }

        PrincipalRole principalRole = new PrincipalRole(role, this);

        this.roles.add(principalRole);

        return principalRole;
    }

    public void blockAction(Action denying) {

        Optional<BlockedAction> optDenials =
            this.denials.stream()
                        .filter(denial -> denial.Action.equals(denying))
                        .findFirst();

        if (optDenials.isEmpty()) {

            this.denials.add(new BlockedAction(this, denying));
        }
    }

    public void grantAction(Action granting) {

        Optional<PrincipalGrant> optUserGrant =
            this.grants.stream()
                       .filter(userGrant -> userGrant.Action.equals(granting))
                       .findFirst();

        if (optUserGrant.isEmpty()) {

            this.grants.add(new PrincipalGrant(this, granting));
        }
    }

    public boolean isGranted(Action Action) {

        if (!this.principalStatus.equals(PrincipalStatus.ACTIVE)) {

            return false;
        }

        if (this.denials.stream()
                        .anyMatch(denial -> denial.Action.equals(Action))) {
            return false;
        }

        for (PrincipalRole principalRole : this.roles) {
            if (principalRole.role.isGranted(Action)) {
                return true;
            }
        }

        for (PrincipalGrant grant : this.grants) {
            if (grant.Action.equals(Action)) {
                return true;
            }
        }

        return false;
    }

    public Set<Action> getGrantedActions() {

        Set<Action> grantedActions = new HashSet<>();
        for (PrincipalGrant grant : this.grants) {
            grantedActions.add(grant.Action);
        }

        return grantedActions;
    }

    public Set<Action> getDeniedActions() {

        Set<Action> deniedActions = new HashSet<>();
        for (BlockedAction denial : this.denials) {
            deniedActions.add(denial.Action);
        }

        return deniedActions;
    }

    public Set<Role> getRoles() {

        Set<Role> roles = new HashSet<>();
        for (PrincipalRole userRole : this.roles) {
            roles.add(userRole.role);
        }

        return roles;
    }

    public boolean removeRole(Role role) {

        var
            optUserRole =
            this.roles.stream()
                      .filter(userRole -> userRole.role.roleId.equals(role.roleId))
                      .findFirst();

        if (optUserRole.isPresent()) {

            this.roles.remove(optUserRole.get());

            return true;
        }

        return false;
    }

    public boolean revokeAction(Action revoking) {

        Optional<PrincipalGrant> optUserGrant =
            this.grants.stream()
                       .filter(userGrant -> userGrant.Action.equals(revoking))
                       .findFirst();

        if (optUserGrant.isPresent()) {

            this.grants.remove(optUserGrant.get());
            return true;
        }

        return false;
    }

    public boolean unblockAction(Action removing) {

        Optional<BlockedAction> optDenial =
            this.denials.stream()
                        .filter(userGrant -> userGrant.Action.equals(removing))
                        .findFirst();

        if (optDenial.isPresent()) {

            this.denials.remove(optDenial.get());
            return true;
        }

        return false;
    }

    public SecurityToken authenticate(String sha256PasswordHex) throws IAMIgnorableException {

        try {

            if (!OperationPortalCrypto.sha256Hex(sha256PasswordHex)
                                      .equals(this.sha256PasswordHex)) {

                throw new IAMIgnorableException(IAMErrors.PASSWORD_AUTHENTICATION_FAILURE);
            }

            return this.generate();

        } catch (Exception e) {

            throw e;
        }
    }

    public SecurityToken change(String newPasswordSha256Hex, String oldPasswordSha256Hex)
        throws IAMIgnorableException {

        if (!OperationPortalCrypto.sha256Hex(oldPasswordSha256Hex)
                                  .equals(this.sha256PasswordHex)) {

            throw new IAMIgnorableException(IAMErrors.PASSWORD_AUTHENTICATION_FAILURE);
        }

        this.sha256PasswordHex = OperationPortalCrypto.sha256Hex(newPasswordSha256Hex);

        return this.generate();
    }

    public SecurityToken reset(String passwordSha256Hex) {

        this.sha256PasswordHex = OperationPortalCrypto.sha256Hex(passwordSha256Hex);
        return this.generate();
    }

    public SecurityToken generate() {

        this.accessKey = new AccessKey(Snowflake.get()
                                                .nextId());
        this.secretKey =
            UUID.randomUUID()
                .toString();

        return new SecurityToken(this.accessKey, this.secretKey);
    }

    public Principal principalStatus(PrincipalStatus principalStatus) {

        this.principalStatus = principalStatus;
        return this;

    }

}
