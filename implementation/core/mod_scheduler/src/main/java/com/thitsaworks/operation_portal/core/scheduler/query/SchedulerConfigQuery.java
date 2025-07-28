package com.thitsaworks.operation_portal.core.scheduler.query;

import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * Interface defining query operations for scheduler configurations.
 */
public interface SchedulerConfigQuery {

    /**
     * Get all scheduler configurations with optional sorting.
     *
     * @param sort the sort specification (can be null for no sorting)
     * @return list of scheduler configurations
     */
    List<SchedulerConfigData> getSchedulerConfigs(Sort sort);

    /**
     * Get all scheduler configurations filtered by active status with optional sorting.
     *
     * @param active filter by active status
     * @param sort the sort specification (can be null for no sorting)
     * @return list of filtered scheduler configurations
     */
    List<SchedulerConfigData> getSchedulerConfigs(boolean active, Sort sort);

    /**
     * Get a specific scheduler configuration by ID.
     *
     * @param configId the ID of the configuration to retrieve
     * @return the scheduler configuration data
     * @throws com.thitsaworks.operation_portal.component.misc.exception.ResourceNotFoundException if config not found
     */
    SchedulerConfigData get(Long configId);

    /**
     * Get a specific scheduler configuration by ID if it exists.
     *
     * @param configId the ID of the configuration to retrieve
     * @return an Optional containing the scheduler configuration if found, empty otherwise
     */
    Optional<SchedulerConfigData> findById(Long configId);
}
