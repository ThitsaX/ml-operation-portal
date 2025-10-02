package com.thitsaworks.operation_portal.core.scheduler.model;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_scheduler_config")
@Getter
@NoArgsConstructor
public class SchedulerConfig extends JpaEntity<SchedulerConfigId> {

    @EmbeddedId
    private SchedulerConfigId schedulerConfigId;

    @Column(name = "name")
    private String name;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "cron_expression")
    private String cronExpression;
    
    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private boolean active;

    public SchedulerConfig(String name, String jobName, String cronExpression, String description) {

        this.schedulerConfigId = new SchedulerConfigId(Snowflake.get().nextId());
        this.name = name;
        this.jobName = jobName;
        this.cronExpression = cronExpression;
        this.description = description;
        this.active = true;
    }

    @Override
    public SchedulerConfigId getId() {

        return this.schedulerConfigId;
    }

    public SchedulerConfig name(String name) {

        this.name = name;
        return this;
    }

    public SchedulerConfig jobName(String jobName) {

        this.jobName = jobName;
        return this;
    }

    public SchedulerConfig cronExpression(String cronExpression) {

        this.cronExpression = cronExpression;
        return this;
    }

    public SchedulerConfig description(String description) {

        this.description = description;
        return this;
    }

    public SchedulerConfig active(boolean active) {

        this.active = active;
        return this;
    }
}
