package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.data.ParticipantBalanceData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import lombok.Value;

public interface GetParticipantBalanceByCurrencyIdQuery {

    @Value
    class Input {

        private int participantCurrencyId;

    }

    @Value
    class Output {

        private ParticipantBalanceData participantBalanceData;

    }

    Output execute(Input input) throws HubServicesException;

}
