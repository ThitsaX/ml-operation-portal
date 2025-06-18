package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.hubuser.identity.HubUserId;

public abstract class ModifyExistingHubUser
        extends AbstractAuditableUseCase<ModifyExistingHubUser.Input, ModifyExistingHubUser.Output> {

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
