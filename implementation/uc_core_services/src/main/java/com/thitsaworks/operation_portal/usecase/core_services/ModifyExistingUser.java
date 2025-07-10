package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface ModifyExistingUser
        extends UseCase<ModifyExistingUser.Input, ModifyExistingUser.Output> {

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
