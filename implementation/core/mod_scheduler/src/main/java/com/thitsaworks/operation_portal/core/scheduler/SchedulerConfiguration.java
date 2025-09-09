package com.thitsaworks.operation_portal.core.scheduler;

import com.thitsaworks.operation_portal.component.infra.mysql.core.CorePersistenceConfiguration;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.misc.MiscConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan("com.thitsaworks.operation_portal.core.scheduler")
@Import(value = {
        MiscConfiguration.class, RedisConfiguration.class, CorePersistenceConfiguration.class})
@EnableScheduling
public class SchedulerConfiguration {

}
