package com.thitsaworks.operation_portal.core.hubuser.query;

import com.thitsaworks.component.common.identifier.HubUserId;
import com.thitsaworks.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.hubuser.data.HubUserData;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;

import java.util.List;

public interface HubUserQuery {

    List<HubUserData> getHubUsers();

    HubUserData get(HubUserId hubUserId) throws HubUserNotFoundException;

}

