package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.hubuser.identity.HubUserId;
import com.thitsaworks.operation_portal.iam.domain.UserRoleType;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.type.PrincipalStatus;

public abstract class CreateNewHubUser
        extends AbstractAuditableUseCase<CreateNewHubUser.Input, CreateNewHubUser.Output> {

    public static record Input(
        String name,
        Email email,
        String password,
        String firstName,
        String lastName,
        String jobTitle,
        UserRoleType userRoleType,
        PrincipalStatus activeStatus
    ) {}

    public static record Output(
        HubUserId hubUserId,
        AccessKey accessKey,
        String secretKey
    ) {}

}
