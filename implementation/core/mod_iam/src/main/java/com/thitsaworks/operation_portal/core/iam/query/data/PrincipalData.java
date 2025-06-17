package com.thitsaworks.operation_portal.core.iam.query.data;

import com.thitsaworks.component.common.type.RealmType;
import com.thitsaworks.component.common.type.UserRoleType;
import com.thitsaworks.component.common.identifier.AccessKey;
import com.thitsaworks.component.common.identifier.PrincipalId;
import com.thitsaworks.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.core.iam.domain.Principal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PrincipalData implements Serializable {

    protected PrincipalId principalId;

    protected AccessKey accessKey;

    protected String secretKey;

    protected RealmType realmType;

    protected RealmId realmId;

    protected UserRoleType userRoleType;

    public PrincipalData(Principal principal) {

        this.principalId = principal.getPrincipalId();
        this.accessKey = principal.getAccessKey();
        this.secretKey = principal.getSecretKey();
        this.realmType = principal.getRealmType();
        this.userRoleType = principal.getUserRoleType();
        this.realmId = principal.getRealmId();
    }

}
