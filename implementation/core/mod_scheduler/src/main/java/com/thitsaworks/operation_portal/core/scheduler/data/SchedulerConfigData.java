package com.thitsaworks.operation_portal.core.scheduler.data;

import com.thitsaworks.operation_portal.core.scheduler.model.SchedulerConfig;

public record SchedulerConfigData(Long id, String name, String cronExpression, String description, boolean active) {

    public SchedulerConfigData(SchedulerConfig config) {

        this(config.getId(), config.getName(), config.getCronExpression(), config.getDescription(), config.isActive());
    }

}