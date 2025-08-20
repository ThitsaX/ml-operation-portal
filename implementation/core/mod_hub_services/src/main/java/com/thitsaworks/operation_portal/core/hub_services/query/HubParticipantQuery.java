package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantAccountData;
import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.util.List;

public interface HubParticipantQuery {

    List<HubParticipantData> getParticipantList() throws HubServicesException;

    HubParticipantData getByName(String name) throws ParticipantException, HubServicesException;

    HubParticipantAccountData getAccountData(Integer participantId, Integer participantCurrencyId)
            throws HubServicesException;



}

