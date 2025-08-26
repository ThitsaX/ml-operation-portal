package com.thitsaworks.operation_portal.core.hub_services.api;

import com.thitsaworks.operation_portal.core.hub_services.support.SettlementParticipant;

import java.util.List;

public class GetParticipants {

    public record Request() {}

    public record Response(List<SettlementParticipant> participants) {}

}

