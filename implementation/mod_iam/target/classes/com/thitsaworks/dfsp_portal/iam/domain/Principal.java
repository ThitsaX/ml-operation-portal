package com.thitsaworks.dfsp_portal.iam.domain;

import com.thitsaworks.dfsp_portal.component.data.jpa.JpaEntity;
import com.thitsaworks.dfsp_portal.component.security.DfspCrypto;
import com.thitsaworks.dfsp_portal.component.util.Snowflake;
import com.thitsaworks.dfsp_portal.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.dfsp_portal.iam.identity.PrincipalId;
import com.thitsaworks.dfsp_portal.iam.identity.RealmId;
import com.thitsaworks.dfsp_portal.iam.query.cache.hazelcast.HazelcastPrincipalCache;
import com.thitsaworks.dfsp_portal.iam.type.PrincipalStatus;
import com.thitsaworks.dfsp_portal.iam.type.RealmType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@EntityListeners(value = {HazelcastPrincipalCache.Updater.class})
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
    protected PrincipalStatus status;

    @Column(name = "user_role_type")
    @Enumerated(EnumType.STRING)
    protected UserRoleType userRoleType;

    @Override
    protected AccessKey getPrimaryId() {

        return this.accessKey;
    }

    public Principal(PrincipalId principalId, RealmType realmType, String sha256PasswordHex, RealmId realmId,
                     UserRoleType userRoleType,PrincipalStatus activeStatus) {

        this.principalId = principalId;
        this.accessKey = new AccessKey(Snowflake.get().nextId());
        this.secretKey = UUID.randomUUID().toString();
        this.realmType = realmType;
        this.realmId = realmId;
        this.status = PrincipalStatus.ACTIVE;
        this.sha256PasswordHex = DfspCrypto.sha256Hex(sha256PasswordHex);
        this.userRoleType = userRoleType;
        this.status=activeStatus;

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

    public Principal modify(PrincipalStatus principalStatus,UserRoleType userRoleType) {

        this.status = principalStatus;
        this.userRoleType=userRoleType;
        return this;

    }

}
