package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantData;
import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantDetailData;
import com.thitsaworks.operation_portal.core.hub_services.data.HubSettlementModelData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.util.List;

public interface HubSettlementModelQuery {

    List<HubSettlementModelData> getSettlementModelList() throws HubServicesException;

    HubSettlementModelData getByName(String name) throws HubServicesException;

}

