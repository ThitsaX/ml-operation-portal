package com.thitsaworks.operation_portal.core.scheduler.model;

import com.thitsaworks.operation_portal.component.common.identifier.JobExecutionLogId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_job_execution_logs")
@Getter
@Setter
@NoArgsConstructor
public class JobExecutionLog extends JpaEntity<JobExecutionLogId> {

    @EmbeddedId
    @Column(name = "job_execution_log_id")
    private JobExecutionLogId jobExecutionLogId;
    
    @Column(name = "job_name", nullable = false)
    private String jobName;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "execution_message", columnDefinition = "TEXT")
    private String executionMessage;

    public JobExecutionLog(String jobName, String status, LocalDateTime startTime) {
        this.jobExecutionLogId = new JobExecutionLogId(Snowflake.get().nextId());
        this.jobName = jobName;
        this.status = status;
        this.startTime = startTime;
    }

    @Override
    public JobExecutionLogId getId() {

        return this.jobExecutionLogId;
    }
    
}
