package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;

public interface ModifySchedulerConfigStatus
        extends UseCase<ModifySchedulerConfigStatus.Input, ModifySchedulerConfigStatus.Output> {

    record Input(SchedulerConfigId schedulerConfigId, boolean active) {

    }

    record Output(boolean modified, SchedulerConfigData schedulerConfigData) {

    }

}
