//package com.thitsaworks.operation_portal.scheduler;
//
//import com.thitsaworks.operation_portal.component.infra.vault.VaultConfiguration;
//import com.thitsaworks.operation_portal.core.hubuser.HubUserConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Import;
//
//@ComponentScan("com.thitsaworks.operation_portal.scheduler")
//@Import(value = {
//        VaultConfiguration.class,
//        VaultBasedApplicationSettings.class, HubUserConfiguration.class})
//public class SchedulerConfiguration {
//
//    @Bean
//    public VaultConfiguration.Settings vaultSettings() {
//
//        return VaultConfiguration.Settings.withPropertyOrEnv();
//    }
//
//    @Bean
//    public AnnouncementScheduler savingPlanScheduler() {
//
//        return new AnnouncementScheduler();
//    }
//
//}
