package com.thitsaworks.dfsp_portal.api.hub_operator.security;

import com.thitsaworks.dfsp_portal.hubuser.identity.HubUserId;
import com.thitsaworks.dfsp_portal.iam.identity.AccessKey;
import lombok.Value;

@Value
public class UserContext {

    HubUserId hubUserId;

    AccessKey accessKey;

}
