package com.thitsaworks.dfsp_portal.scheduler;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SchedulerApplication {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(SchedulerConfiguration.class, MySqlDbSettings.class);
        applicationContext.refresh();

        AnnouncementScheduler removeAnnouncementScheduler = applicationContext.getBean(AnnouncementScheduler.class);
        removeAnnouncementScheduler.runScheduler();
        System.exit(0);
    }

}
