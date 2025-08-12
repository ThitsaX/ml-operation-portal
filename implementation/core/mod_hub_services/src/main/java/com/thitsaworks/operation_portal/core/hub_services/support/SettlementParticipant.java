package com.thitsaworks.operation_portal.core.hub_services.support;

import java.util.List;

public record SettlementParticipant(
        Integer id,
        List<SettlementAccount> accounts
) {}
