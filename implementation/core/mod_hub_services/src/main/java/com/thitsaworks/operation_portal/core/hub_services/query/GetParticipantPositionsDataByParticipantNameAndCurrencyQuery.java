package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.data.ParticipantPositionData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import lombok.Value;

import java.util.List;

public interface GetParticipantPositionsDataByParticipantNameAndCurrencyQuery {

    @Value
    class Input {

        private String dfspId;
        private String currencyId;

    }

    @Value
    class Output {

        private List<ParticipantPositionData> participantPositionData;

    }

    Output execute(Input input) throws HubServicesException;

}
