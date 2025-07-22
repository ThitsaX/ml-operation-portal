package com.thitsaworks.operation_portal.core.hubuser.command;

import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;

public interface CreateHubUserCommand {

    record Input(String name,
                 Email email,
                 String firstName,
                 String lastName,
                 String jobTitle) {}

    record Output(boolean created, HubUserId hubUserId) {}

    Output execute(Input input) throws HubUserException;
}
