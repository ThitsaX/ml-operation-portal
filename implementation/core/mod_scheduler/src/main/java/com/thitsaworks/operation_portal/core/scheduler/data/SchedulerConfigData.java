package com.thitsaworks.operation_portal.core.scheduler.data;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.core.scheduler.model.SchedulerConfig;

public record SchedulerConfigData(SchedulerConfigId schedulerConfigId, String name, String jobName,
                                  String cronExpression, String description, boolean active) {

    public SchedulerConfigData(SchedulerConfig config) {

        this(config.getSchedulerConfigId(),
             config.getName(),
             config.getJobName(),
             config.getCronExpression(),
             config.getDescription(),
             config.isActive());
    }

}