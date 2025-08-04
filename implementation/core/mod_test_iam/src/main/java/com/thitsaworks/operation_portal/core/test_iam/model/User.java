package com.thitsaworks.operation_portal.core.test_iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.security.OperationPortalCrypto;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import com.thitsaworks.operation_portal.core.test_iam.listener.UserListener;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
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
@EntityListeners(UserListener.class)
@Table(name = "tbl_user")
@NoArgsConstructor
@Getter
public class User extends JpaEntity<UserId> {

    @EmbeddedId
    protected UserId userId;

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
        mappedBy = "user",
        cascade = ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER)
    protected Set<UserRole> roles = new HashSet<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(
        mappedBy = "user",
        cascade = ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER)
    protected Set<UserGrant> grants = new HashSet<>();

    @Getter(AccessLevel.NONE)
    @OneToMany(
        mappedBy = "user",
        cascade = ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER)
    protected Set<BlockedAction> denials = new HashSet<>();

    public User(UserId userId,
                RealmType realmType,
                String sha256PasswordHex,
                RealmId realmId,
                PrincipalStatus principalStatus) {

        this.userId = userId;
        this.accessKey = new AccessKey(Snowflake.get()
                                                .nextId());
        this.secretKey = UUID.randomUUID()
                             .toString();
        this.realmType = realmType;
        this.realmId = realmId;
        this.sha256PasswordHex = OperationPortalCrypto.sha256Hex(sha256PasswordHex);
        this.principalStatus = principalStatus;
    }

    public UserRole assignRole(Role role) throws IAMException {

        if (this.roles.stream()
                      .anyMatch(existing -> existing.role.equals(role))) {

            throw new IAMException(IAMErrors.ROLE_ALREADY_ASSIGN_TO_USER);
        }

        UserRole userRole = new UserRole(role, this);

        this.roles.add(userRole);

        return userRole;
    }

    public SecurityToken authenticate(String password) {

        var hash = OperationPortalCrypto.sha256Hex(password);

        if (this.sha256PasswordHex.equals(hash)) {

            return this.generate();

        } else {

//            throw new IAMException(IAMException.ErrorCodes.UnableToAuthenticate);

            return null;
        }
    }

    public SecurityToken change(String oldPasswordSha256Hex, String newPasswordSha256Hex) {

        if (!OperationPortalCrypto.sha256Hex(oldPasswordSha256Hex)
                                  .equals(this.sha256PasswordHex)) {

            //throw new IAMIgnorableException(IAMErrors.PASSWORD_AUTHENTICATION_FAILURE);
        }

        this.sha256PasswordHex = OperationPortalCrypto.sha256Hex(newPasswordSha256Hex);

        return this.generate();
    }

    public void blockAction(IAMAction denying) {

        Optional<BlockedAction> optDenials =
            this.denials.stream()
                        .filter(denial -> denial.IAMAction.equals(denying))
                        .findFirst();

        if (optDenials.isEmpty()) {

            this.denials.add(new BlockedAction(this, denying));
        }
    }

    @Override
    public UserId getId() {

        return this.userId;
    }

    public void grantAction(IAMAction granting) {

        Optional<UserGrant> optUserGrant =
            this.grants.stream()
                       .filter(userGrant -> userGrant.IAMAction.equals(granting))
                       .findFirst();

        if (optUserGrant.isEmpty()) {

            this.grants.add(new UserGrant(this, granting));
        }
    }

    public boolean isGranted(IAMAction IAMAction) {

        if (!this.principalStatus.equals(PrincipalStatus.ACTIVE)) {

            return false;
        }

        if (this.denials.stream()
                        .anyMatch(denial -> denial.IAMAction.equals(IAMAction))) {
            return false;
        }

        for (UserRole userRole : this.roles) {
            if (userRole.role.isGranted(IAMAction)) {
                return true;
            }
        }

        for (UserGrant grant : this.grants) {
            if (grant.IAMAction.equals(IAMAction)) {
                return true;
            }
        }

        return false;
    }

    public Set<IAMAction> getGrantedActions() {

        Set<IAMAction> grantedActions = new HashSet<>();
        for (UserGrant grant : this.grants) {
            grantedActions.add(grant.IAMAction);
        }

        return grantedActions;
    }

    public Set<IAMAction> getDeniedActions() {
        Set<IAMAction> deniedActions = new HashSet<>();
        for (BlockedAction denial : this.denials) {
            deniedActions.add(denial.IAMAction);
        }

        return deniedActions;
    }

    public Set<Role> getRoles() {

        Set<Role> roles = new HashSet<>();
        for (UserRole userRole : this.roles) {
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

    public SecurityToken reset(String passwordSha256Hex) {

        this.sha256PasswordHex = OperationPortalCrypto.sha256Hex(passwordSha256Hex);
        return this.generate();
    }

    public boolean revokeAction(IAMAction revoking) {

        Optional<UserGrant> optUserGrant =
            this.grants.stream()
                       .filter(userGrant -> userGrant.IAMAction.equals(revoking))
                       .findFirst();

        if (optUserGrant.isPresent()) {

            this.grants.remove(optUserGrant.get());
            return true;
        }

        return false;
    }

    public boolean unblockAction(IAMAction removing) {

        Optional<BlockedAction> optDenial =
            this.denials.stream()
                        .filter(userGrant -> userGrant.IAMAction.equals(removing))
                        .findFirst();

        if (optDenial.isPresent()) {

            this.denials.remove(optDenial.get());
            return true;
        }

        return false;
    }

    public SecurityToken generate() {

        this.accessKey = new AccessKey(Snowflake.get()
                                                .nextId());
        this.secretKey = UUID.randomUUID()
                             .toString();

        return new SecurityToken(this.accessKey, this.secretKey);
    }

    public User modify(PrincipalStatus principalStatus, UserRoleType userRoleType) {

        this.principalStatus = principalStatus;
        return this;

    }

}
