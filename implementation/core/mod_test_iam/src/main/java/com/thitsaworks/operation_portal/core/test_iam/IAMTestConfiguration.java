package com.thitsaworks.operation_portal.core.test_iam;

import com.thitsaworks.operation_portal.component.infra.mysql.core.CorePersistenceConfiguration;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.misc.MiscConfiguration;
import com.thitsaworks.operation_portal.core.test_iam.engine.IAMEngine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.core.test_iam")
@Import(value = {
    MiscConfiguration.class, RedisConfiguration.class, CorePersistenceConfiguration.class
})
@RequiredArgsConstructor
public class IAMTestConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(IAMTestConfiguration.class);

    private final IAMEngine iamEngine;

    @PostConstruct
    public void bootstrapIAMEngine() {
        try {

            LOG.info("Starting IAMEngine bootstrap...");
            this.iamEngine.bootstrap();
            LOG.info("IAMEngine bootstrap completed successfully");

        } catch (Exception e) {

            LOG.error("Failed to bootstrap IAMEngine", e);
            throw new IllegalStateException("Failed to bootstrap IAMEngine", e);
        }
    }
}
