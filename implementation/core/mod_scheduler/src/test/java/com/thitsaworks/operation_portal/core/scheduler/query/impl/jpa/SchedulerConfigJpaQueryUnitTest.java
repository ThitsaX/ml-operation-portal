package com.thitsaworks.operation_portal.core.scheduler.query.impl.jpa;

import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.core.scheduler.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.scheduler.SchedulerConfiguration;
import com.thitsaworks.operation_portal.core.scheduler.TestSettings;
import com.thitsaworks.operation_portal.core.scheduler.exception.ResourceNotFoundException;
import com.thitsaworks.operation_portal.core.scheduler.model.SchedulerConfig;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.SchedulerConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SchedulerConfiguration.class, TestSettings.class, RedisConfiguration.class})
@Transactional
public class SchedulerConfigJpaQueryUnitTest extends BaseVaultSetUpTest {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerConfigJpaQueryUnitTest.class);

    @Autowired
    private SchedulerConfigRepository schedulerConfigRepository;
    
    @Autowired
    private SchedulerConfigJpaQueryHandler queryHandler;
    
    @Test
    void getSchedulerConfigs_ShouldReturnAllConfigs() {

        var result = queryHandler.getSchedulerConfigs(Sort.unsorted());

        LOG.info("Configs: {}", result);
        
    }
    
    @Test
    void get_ShouldReturnConfigWhenExists() {

        var result = queryHandler.get(1L);

        LOG.info("Config: {}", result);
        
    }
    
    @Test
    void get_ShouldThrowWhenConfigNotFound() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            queryHandler.get(999L);
        });
    }
}
