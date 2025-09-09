package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface RemoveSchedulerConfig
    extends UseCase<RemoveSchedulerConfig.Input, RemoveSchedulerConfig.Output> {

    record Input(Long id) { }

    record Output(boolean deleted) { }
}