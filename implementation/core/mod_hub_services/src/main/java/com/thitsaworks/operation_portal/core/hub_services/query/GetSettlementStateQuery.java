package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.data.SettlementStateData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;

import java.util.List;

public interface GetSettlementStateQuery {

    record Input() { }

    record Output(List<SettlementStateData> settlementStates) { }

    Output execute(Input input) throws HubServicesException;

}
