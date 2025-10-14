package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowStateData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;

import java.util.List;

public interface GetSettlementWindowStateQuery {

    record Input() { }

    record Output(List<SettlementWindowStateData> settlementWindowStates ) { }

    Output execute(Input input) throws HubServicesException;

}
