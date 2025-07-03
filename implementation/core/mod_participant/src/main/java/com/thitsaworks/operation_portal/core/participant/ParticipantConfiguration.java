package com.thitsaworks.operation_portal.core.participant;

import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.infra.mysql.core.CorePersistenceConfiguration;
import com.thitsaworks.operation_portal.component.misc.MiscConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.core.participant")
@Import(value = {
        MiscConfiguration.class, RedisConfiguration.class, CorePersistenceConfiguration.class})
public class ParticipantConfiguration {

}
