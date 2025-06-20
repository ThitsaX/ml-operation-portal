package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.component.type.Email;

public abstract class CreateNewHubUser
        extends AbstractAuditableUseCase<CreateNewHubUser.Input, CreateNewHubUser.Output> {

    public record Input(
        String name,
        Email email,
        String password,
        String firstName,
        String lastName,
        String jobTitle,
        UserRoleType userRoleType,
        PrincipalStatus activeStatus
    ) {}

    public record Output(
        HubUserId hubUserId,
        AccessKey accessKey,
        String secretKey
    ) {}

}
