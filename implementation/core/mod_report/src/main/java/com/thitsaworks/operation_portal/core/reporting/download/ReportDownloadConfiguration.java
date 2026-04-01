package com.thitsaworks.operation_portal.core.reporting.download;

import com.thitsaworks.operation_portal.component.infra.mysql.core.CorePersistenceConfiguration;
import com.thitsaworks.operation_portal.component.misc.MiscConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@ComponentScan("com.thitsaworks.operation_portal.core.reporting.download")
@Import(value = {
        MiscConfiguration.class,
        CorePersistenceConfiguration.class
})
public class ReportDownloadConfiguration {

    @Bean(name = "reportGenerationTaskExecutor")
    public ThreadPoolTaskExecutor reportGenerationTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(0);
        executor.setThreadNamePrefix("report-gen-");
        executor.initialize();
        return executor;
    }
}
