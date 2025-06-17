package com.thitsaworks.operation_portal.core.iam.domain;

import com.thitsaworks.component.common.type.RealmType;
import com.thitsaworks.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.security.DfspCrypto;
import com.thitsaworks.operation_portal.component.util.Snowflake;
import com.thitsaworks.operation_portal.core.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.component.common.identifier.AccessKey;
import com.thitsaworks.component.common.identifier.PrincipalId;
import com.thitsaworks.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.core.iam.query.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.query.cache.hazelcast.HazelcastPrincipalCache;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@EntityListeners(value = {PrincipalCache.Updater.class})
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

    @Column(name = "user_role_type")
    @Enumerated(EnumType.STRING)
    protected UserRoleType userRoleType;

    @Override
    public AccessKey getId() {

        return this.accessKey;
    }

    public Principal(PrincipalId principalId, RealmType realmType, String sha256PasswordHex, RealmId realmId,
                     UserRoleType userRoleType) {

        this.principalId = principalId;
        this.accessKey = new AccessKey(Snowflake.get().nextId());
        this.secretKey = UUID.randomUUID().toString();
        this.realmType = realmType;
        this.realmId = realmId;
        this.sha256PasswordHex = DfspCrypto.sha256Hex(sha256PasswordHex);
        this.userRoleType = userRoleType;

    }

    public SecurityToken authenticate(String sha256PasswordHex) throws PasswordAuthenticationFailureException {

        try {

            if (!DfspCrypto.sha256Hex(sha256PasswordHex).equals(this.sha256PasswordHex)) {

                throw new PasswordAuthenticationFailureException();
            }

            return this.generate();

        } catch (Exception e) {

            throw e;
        }
    }

    public SecurityToken change(String newPasswordSha256Hex, String oldPasswordSha256Hex)
            throws PasswordAuthenticationFailureException {

        if (!DfspCrypto.sha256Hex(oldPasswordSha256Hex).equals(this.sha256PasswordHex)) {

            throw new PasswordAuthenticationFailureException();
        }

        this.sha256PasswordHex = DfspCrypto.sha256Hex(newPasswordSha256Hex);

        return this.generate();
    }

    public SecurityToken reset(String passwordSha256Hex) {

        this.sha256PasswordHex = DfspCrypto.sha256Hex(passwordSha256Hex);
        return this.generate();
    }

    public SecurityToken generate() {

        this.accessKey = new AccessKey(Snowflake.get().nextId());
        this.secretKey = UUID.randomUUID().toString();

        return new SecurityToken(this.accessKey, this.secretKey);
    }

//    public Principal modify(PrincipalStatus principalStatus,UserRoleType userRoleType) {
//
//        this.status = principalStatus;
//        this.userRoleType=userRoleType;
//        return this;
//
//    }

}
