//package com.thitsaworks.operation_portal.scheduler;
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.context.annotation.Import;
//
//@Import(value = {SchedulerConfiguration.class})
//public class SchedulerApplication {
//
//    public static void main(String[] args) {
//
//        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
//
//        applicationContext.register(SchedulerConfiguration.class);
//
//        applicationContext.refresh();
//
//        AnnouncementScheduler removeAnnouncementScheduler = applicationContext.getBean(AnnouncementScheduler.class);
//
//        removeAnnouncementScheduler.runScheduler();
//
//        System.exit(0);
//    }
//
//}
