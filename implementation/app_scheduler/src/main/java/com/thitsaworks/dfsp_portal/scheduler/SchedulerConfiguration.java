package com.thitsaworks.dfsp_portal.scheduler;

import com.thitsaworks.dfsp_portal.hubuser.HubUserConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.dfsp_portal.scheduler")
@Import(value = {HubUserConfiguration.class})
public class SchedulerConfiguration {

    @Bean
    public AnnouncementScheduler savingPlanScheduler() {

        return new AnnouncementScheduler();
    }

}
