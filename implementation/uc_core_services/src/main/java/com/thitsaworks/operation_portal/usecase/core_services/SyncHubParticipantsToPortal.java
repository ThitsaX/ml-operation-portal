package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface SyncHubParticipantsToPortal
        extends UseCase<SyncHubParticipantsToPortal.Input, SyncHubParticipantsToPortal.Output> {

    public record Input(
    ) {}

    public record Output(boolean synced
    ) {}

}
