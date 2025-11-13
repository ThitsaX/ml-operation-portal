package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.data.ParticipantBalanceData;
import com.thitsaworks.operation_portal.core.hub_services.data.ParticipantLimitData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import lombok.Value;

public interface GetParticipantLimitByCurrencyIdQuery {

    @Value
    class Input {

        private String participantName;

        private String currencyId;

    }

    @Value
    class Output {

        private ParticipantLimitData participantLimitData;

    }

    Output execute(Input input) throws HubServicesException;

}
