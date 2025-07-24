package com.thitsaworks.operation_portal.core.test_iam.model;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.BlockedActionId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.IAMuserId;
import com.thitsaworks.operation_portal.component.common.type.iamtesttype.IAMusername;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "tbl_user")
@NoArgsConstructor
@Getter
public class IAMuser extends JpaEntity<IAMuserId> {
    @EmbeddedId
    protected IAMuserId iamuserId;

    @Column(name = "username")
    @Convert(converter = IAMusername.JpaConverter.class)
    protected IAMusername iamusername;

    @Column(name = "name")
    protected String name;

    @Column(name = "password_hash")
    protected String passwordHash;

    @Column(name = "sys_password_hash")
    protected String sysPasswordHash;

    @Column(name = "access_key")
    protected String accessKey;

    @Column(name = "secret_key")
    protected String secretKey;

    @Column(name = "active")
    protected Boolean active = true;

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

    public IAMuser(IAMusername iamusername, String name, String password) {

        // check not null and throw error
        assert name != null : "name is required!";
        assert iamusername != null : "username is required!";
        assert password != null : "password is required!";

        this.iamuserId = new IAMuserId(Snowflake.get().nextId());
        this.name = name;
        this.passwordHash = DigestUtils.sha256Hex(password);
        this.accessKey = DigestUtils.sha256Hex(UUID.randomUUID().toString());
        this.secretKey = DigestUtils.sha256Hex(UUID.randomUUID().toString());
    }

    public UserRole assignRole(Role role) {

        if (this.roles.stream().anyMatch(existing -> existing.role.equals(role))) {

//            throw new IAMException(IAMException.ErrorCodes.RoleAlreadyAssignedToUser);
        }

        UserRole userRole = new UserRole(role, this);

        this.roles.add(userRole);

        return userRole;
    }

//    public Token authenticate(String password) {
//
//        if (!this.active) {
//
//            throw new IAMException(IAMException.ErrorCodes.UserIsInactive);
//        }
//
//        var reset = false;
//        var hash = DigestUtils.sha256Hex(password);
//
//        if (this.passwordHash.equals(hash)) {
//
//            this.sysPasswordHash = null;
//
//        } else if (this.sysPasswordHash != null) {
//
//            if (this.sysPasswordHash.equals(hash)) {
//                this.sysPasswordHash = null;
//                reset = true;
//            }
//        } else {
//
//            throw new IAMException(IAMException.ErrorCodes.UnableToAuthenticate);
//        }
//
//        this.accessKey = DigestUtils.sha256Hex(UUID.randomUUID().toString());
//        this.secretKey = DigestUtils.sha256Hex(UUID.randomUUID().toString());
//
//        return new Token(this.accessKey, this.secretKey, reset);
//    }
//
//    public Token changePassword(String currentPassword, String newPassword) {
//
//        if (newPassword == null || newPassword.isEmpty()) {
//
//            throw new IAMException(IAMException.ErrorCodes.InvalidPassword);
//        }
//
//        var hash = DigestUtils.sha256Hex(currentPassword);
//
//        if (this.sysPasswordHash != null && !this.sysPasswordHash.equals(hash) && !this.passwordHash.equals(hash)) {
//            throw new IAMException(IAMException.ErrorCodes.FailToChangePassword);
//        } else if (!this.passwordHash.equals(hash)) {
//            throw new IAMException(IAMException.ErrorCodes.FailToChangePassword);
//        }
//
//        this.passwordHash = DigestUtils.sha256Hex(newPassword);
//        this.accessKey = DigestUtils.sha256Hex(UUID.randomUUID().toString());
//        this.secretKey = DigestUtils.sha256Hex(UUID.randomUUID().toString());
//        this.sysPasswordHash = null;
//
//        return new Token(this.accessKey, this.secretKey, false);
//    }

    public void blockAction(Action denying) {

        Optional<BlockedAction> optDenials =
            this.denials.stream().filter(denial -> denial.action.equals(denying)).findFirst();

        if (optDenials.isEmpty()) {

            this.denials.add(new BlockedAction(this, denying));
        }
    }

    @Override
    public IAMuserId getId() {

        return null;
    }

    public void grantAction(Action granting) {

        Optional<UserGrant> optUserGrant =
            this.grants.stream().filter(userGrant -> userGrant.action.equals(granting)).findFirst();

        if (optUserGrant.isEmpty()) {

            this.grants.add(new UserGrant(this, granting));
        }
    }

    public boolean isGranted(Action action) {

        if (!this.active) {

            return false;
        }

        if (this.denials.stream().anyMatch(denial -> denial.action.equals(action))) {
            return false;
        }

        for (UserRole userRole : this.roles) {
            if (userRole.role.isGranted(action)) {
                return true;
            }
        }

        for (UserGrant grant : this.grants) {
            if (grant.action.equals(action)) {
                return true;
            }
        }

        return false;
    }

    public IAMuser name(String name) {

        if (name == null || name.isEmpty() || name.isBlank()) {

//            throw new IAMException(IAMException.ErrorCodes.InvalidName);
        }

        this.name = name;

        return this;

    }

    public boolean removeRole(Role role) {

        var optUserRole = this.roles.stream().filter(userRole -> userRole.role.roleId.equals(role.roleId)).findFirst();

        if (optUserRole.isPresent()) {

            this.roles.remove(optUserRole.get());

            return true;
        }

        return false;
    }

//    public String resetPassword() {
//
//        String genPassword = RandomString.generate(8);
//
//        this.sysPasswordHash = DigestUtils.sha256Hex(genPassword);
//
//        return genPassword;
//    }

    public boolean revokeAction(Action revoking) {

        Optional<UserGrant> optUserGrant =
            this.grants.stream().filter(userGrant -> userGrant.action.equals(revoking)).findFirst();

        if (optUserGrant.isPresent()) {

            this.grants.remove(optUserGrant.get());
            return true;
        }

        return false;
    }

    public boolean toggle() {

        this.active = !this.active;

        return this.active;
    }

    public boolean unblockAction(Action removing) {

        Optional<BlockedAction> optDenial =
            this.denials.stream().filter(userGrant -> userGrant.action.equals(removing)).findFirst();

        if (optDenial.isPresent()) {

            this.denials.remove(optDenial.get());
            return true;
        }

        return false;
    }

    public IAMuser username(IAMusername iamusername) {

//        if (username == null) {
//
//            throw new IAMException(IAMException.ErrorCodes.InvalidUsername);
//        }

        this.iamusername = iamusername;

        return this;
    }


}

