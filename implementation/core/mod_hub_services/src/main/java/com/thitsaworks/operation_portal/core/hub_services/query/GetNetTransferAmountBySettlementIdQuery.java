package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.data.SettlementWindowInfoData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import lombok.Value;

import java.util.List;

public interface GetNetTransferAmountBySettlementIdQuery {

    @Value
    class Input {

        private int settlementId;

    }

    @Value
    class Output {

        private List<SettlementWindowInfoData> windowInfoList;

    }

    Output execute(Input input) throws HubServicesException;

}
