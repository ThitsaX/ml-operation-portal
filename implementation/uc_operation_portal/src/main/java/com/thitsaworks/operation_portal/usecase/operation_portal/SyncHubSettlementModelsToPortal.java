package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface SyncHubSettlementModelsToPortal
        extends UseCase<SyncHubSettlementModelsToPortal.Input, SyncHubSettlementModelsToPortal.Output> {

    public record Input(
    ) {}

    public record Output(boolean synced
    ) {}

}
