package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;

public interface ModifyParticipantDescriptionQuery {

    Output execute(Input input) throws HubServicesException;

    record Input(String participantName,
                 String description) { }

    record Output(boolean updated) { }

}
