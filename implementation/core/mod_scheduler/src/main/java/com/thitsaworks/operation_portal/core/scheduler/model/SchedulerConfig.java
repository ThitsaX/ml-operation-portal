package com.thitsaworks.operation_portal.core.scheduler.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "scheduler_configs")
public class SchedulerConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Column(name = "cron_expression", nullable = false)
    private String cronExpression;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
    
    public SchedulerConfig() {}
    
    public SchedulerConfig(String name, String cronExpression, String description) {
        this.name = name;
        this.cronExpression = cronExpression;
        this.description = description;
    }
}
