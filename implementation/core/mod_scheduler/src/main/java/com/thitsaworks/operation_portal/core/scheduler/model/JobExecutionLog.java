package com.thitsaworks.operation_portal.core.scheduler.model;

import com.thitsaworks.operation_portal.component.common.identifier.JobExecutionLogId;
import com.thitsaworks.operation_portal.component.common.type.JobStatus;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_job_execution_log")
@Getter
@Setter
@NoArgsConstructor
public class JobExecutionLog extends JpaEntity<JobExecutionLogId> {

    @EmbeddedId
    private JobExecutionLogId jobExecutionLogId;
    
    @Column(name = "job_name")
    private String jobName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "execution_message")
    private String executionMessage;

    public JobExecutionLog(String jobName, JobStatus jobStatus, LocalDateTime startTime) {
        this.jobExecutionLogId = new JobExecutionLogId(Snowflake.get().nextId());
        this.jobName = jobName;
        this.jobStatus = jobStatus;
        this.startTime = startTime;
    }

    @Override
    public JobExecutionLogId getId() {

        return this.jobExecutionLogId;
    }

    public void status(JobStatus jobStatus) {

        this.jobStatus = jobStatus;
    }

    public void endTime(LocalDateTime endTime) {

        this.endTime = endTime;
    }

    public void executionMessage(String executionMessage) {

        this.executionMessage = executionMessage;
    }
    
}
