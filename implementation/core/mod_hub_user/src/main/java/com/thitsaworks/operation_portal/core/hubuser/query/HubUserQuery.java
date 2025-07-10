package com.thitsaworks.operation_portal.core.hubuser.query;

import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.core.hubuser.data.HubUserData;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;

import java.util.List;

public interface HubUserQuery {

    List<HubUserData> getHubUsers();

    HubUserData get(HubUserId hubUserId) throws HubUserException;

}

