package com.thitsaworks.operation_portal.api.hub_operator.security;

import com.thitsaworks.operation_portal.hubuser.identity.HubUserId;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import lombok.Value;

@Value
public class UserContext {

    HubUserId hubUserId;

    AccessKey accessKey;

}
