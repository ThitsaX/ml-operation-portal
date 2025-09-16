package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowInfoData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import lombok.Value;

import java.util.List;

public interface GetNetTransferAmountByWindowIdQuery {

    @Value
    class Input {

        private int settlementWindowId;

    }

    @Value
    class Output {

        private List<SettlementWindowInfoData> windowInfoList;

    }

    Output execute(Input input) throws HubServicesException;

}
