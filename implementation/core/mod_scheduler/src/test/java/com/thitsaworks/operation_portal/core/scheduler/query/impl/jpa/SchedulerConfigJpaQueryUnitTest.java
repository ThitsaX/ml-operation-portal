package com.thitsaworks.operation_portal.core.scheduler.query.impl.jpa;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.scheduler.exception.ResourceNotFoundException;
import com.thitsaworks.operation_portal.core.scheduler.model.SchedulerConfig;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.SchedulerConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SchedulerConfigJpaQueryUnitTest extends EnvAwareUnitTest {

    @Autowired
    private SchedulerConfigRepository schedulerConfigRepository;
    
    @Autowired
    private SchedulerConfigJpaQueryHandler queryHandler;
    
    private SchedulerConfig testConfig1;
    private SchedulerConfig testConfig2;
    
    @BeforeEach
    void setUp() {
        // Clear existing data
        schedulerConfigRepository.deleteAll();
        
        // Create and save test data
        testConfig1 = new SchedulerConfig();
        testConfig1.setName("task1");
        testConfig1.setCronExpression("0 0/5 * * * ?");
        testConfig1 = schedulerConfigRepository.save(testConfig1);
        
        testConfig2 = new SchedulerConfig();
        testConfig2.setName("task2");
        testConfig2.setCronExpression("0 0/10 * * * ?");
        testConfig2 = schedulerConfigRepository.save(testConfig2);
        
        // Flush to ensure data is written to the database
        schedulerConfigRepository.flush();
    }
    
    @Test
    void getSchedulerConfigs_ShouldReturnAllConfigs() {
        // Act
        var result = queryHandler.getSchedulerConfigs(Sort.unsorted());
        
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.name().equals("task1")));
        assertTrue(result.stream().anyMatch(c -> c.name().equals("task2")));
    }
    
    @Test
    void get_ShouldReturnConfigWhenExists() {
        // Act
        var result = queryHandler.get(testConfig1.getId());
        
        // Assert
        assertNotNull(result);
        assertEquals("task1", result.name());
        assertEquals("0 0/5 * * * ?", result.cronExpression());
    }
    
    @Test
    void get_ShouldThrowWhenConfigNotFound() {
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            queryHandler.get(999L);
        });
    }
}
