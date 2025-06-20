package com.thitsaworks.operation_portal.api.hub_operator.security;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;

public record UserContext(HubUserId hubUserId, AccessKey accessKey) {
}
