package com.thitsaworks.operation_portal.api.operation.portal.security;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;

public record UserContext(UserId userId, AccessKey accessKey) { }
