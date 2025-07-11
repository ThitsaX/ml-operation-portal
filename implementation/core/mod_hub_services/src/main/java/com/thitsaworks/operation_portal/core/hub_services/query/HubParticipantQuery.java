package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.util.List;

public interface HubParticipantQuery {

    List<HubParticipantData> getParticipants() throws HubServicesException;

    HubParticipantData getByName(String name) throws ParticipantException, HubServicesException;

}

