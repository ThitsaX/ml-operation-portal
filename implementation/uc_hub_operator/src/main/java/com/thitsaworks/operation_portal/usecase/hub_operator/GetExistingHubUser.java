package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.component.type.Email;

public interface GetExistingHubUser
        extends UseCase<GetExistingHubUser.Input, GetExistingHubUser.Output> {

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
