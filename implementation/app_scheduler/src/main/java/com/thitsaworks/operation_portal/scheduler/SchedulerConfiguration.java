package com.thitsaworks.operation_portal.scheduler;

import com.thitsaworks.operation_portal.hubuser.HubUserConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.scheduler")
@Import(value = {HubUserConfiguration.class})
public class SchedulerConfiguration {

    @Bean
    public AnnouncementScheduler savingPlanScheduler() {

        return new AnnouncementScheduler();
    }

}
