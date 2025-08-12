package com.thitsaworks.operation_portal.core.hub_services.support;

import java.util.List;

public record SettlementWindow(
        Integer id,
        String reason,
        String state,
        String createdDate,
        String changedDate,
        List<SettlementContent> content
) {}
