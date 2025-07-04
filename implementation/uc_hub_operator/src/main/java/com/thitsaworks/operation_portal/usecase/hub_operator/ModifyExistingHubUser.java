package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface ModifyExistingHubUser
        extends UseCase<ModifyExistingHubUser.Input, ModifyExistingHubUser.Output> {

    public record Input(
            HubUserId hubUserId,
            String name,
            String firstName,
            String lastName,
            String jobTitle
    ) {}

    public record Output(
            boolean modified,
            HubUserId hubUserId
    ) {}
}
