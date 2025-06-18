package com.thitsaworks.operation_portal.core.hubuser.command;

import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserNotFoundException;

public interface ModifyHubUser {

    record Input(
            HubUserId hubUserId,
            String name,
            String firstName,
            String lastName,
            String jobTitle) {}

    record Output(HubUserId hubUserId, boolean modified) {}

    Output execute(Input input) throws HubUserNotFoundException;

}
