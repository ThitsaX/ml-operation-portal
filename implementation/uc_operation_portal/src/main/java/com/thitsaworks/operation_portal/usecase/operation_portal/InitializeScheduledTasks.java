package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface InitializeScheduledTasks
    extends UseCase<InitializeScheduledTasks.Input, InitializeScheduledTasks.Output> {

    record Input() { }

    record Output(boolean initialized) { }
}
