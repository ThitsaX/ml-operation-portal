package com.thitsaworks.operation_portal.core.scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class SchedulerThreadConfiguration implements SchedulingConfigurer {

    @Bean
    public ThreadPoolTaskScheduler scheduledTaskScheduler() {

        ThreadPoolTaskScheduler ts = new ThreadPoolTaskScheduler();
        ts.setPoolSize(5); //Define the number of threads
        ts.setThreadNamePrefix("sched-");
        ts.setRemoveOnCancelPolicy(true);
        ts.initialize();
        return ts;
    }

    // Make @Scheduled use the scheduler above
    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {

        registrar.setTaskScheduler(scheduledTaskScheduler());
    }

}
