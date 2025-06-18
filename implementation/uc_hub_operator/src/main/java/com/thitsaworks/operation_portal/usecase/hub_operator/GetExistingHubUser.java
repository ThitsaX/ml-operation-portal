package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.hubuser.identity.HubUserId;

public abstract class GetExistingHubUser
        extends AbstractAuditableUseCase<GetExistingHubUser.Input, GetExistingHubUser.Output> {

    public record Input(
            HubUserId hubUserId
    ) {}

    public record Output(
            HubUserId hubUserId,
            String name,
            Email email,
            String firstName,
            String lastName,
            String jobTitle,
            Long createdDate
    ) {}
}
