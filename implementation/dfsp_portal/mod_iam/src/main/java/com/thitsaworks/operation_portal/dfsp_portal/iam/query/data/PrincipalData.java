package com.thitsaworks.operation_portal.dfsp_portal.iam.query.data;

import com.thitsaworks.operation_portal.dfsp_portal.iam.domain.UserRoleType;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.PrincipalId;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.RealmId;
import com.thitsaworks.operation_portal.dfsp_portal.iam.domain.Principal;
import com.thitsaworks.operation_portal.dfsp_portal.iam.type.PrincipalStatus;
import com.thitsaworks.operation_portal.dfsp_portal.iam.type.RealmType;
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

    protected PrincipalStatus status;
    protected UserRoleType userRoleType;

    public PrincipalData(Principal principal) {

        this.principalId = principal.getPrincipalId();
        this.accessKey = principal.getAccessKey();
        this.secretKey = principal.getSecretKey();
        this.realmType = principal.getRealmType();
        this.status = principal.getStatus();
        this.userRoleType = principal.getUserRoleType();
        this.realmId = principal.getRealmId();
    }

}
