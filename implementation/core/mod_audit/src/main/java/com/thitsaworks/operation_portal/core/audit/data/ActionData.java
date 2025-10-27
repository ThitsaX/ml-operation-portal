package com.thitsaworks.operation_portal.core.audit.data;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;

import java.time.Instant;

public record ActionData(ActionId actionId,
                         String name,
                         Instant createdDate
) { }
