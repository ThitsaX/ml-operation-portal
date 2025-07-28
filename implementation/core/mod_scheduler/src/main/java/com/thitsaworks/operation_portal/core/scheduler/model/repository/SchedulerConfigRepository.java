package com.thitsaworks.operation_portal.core.scheduler.model.repository;

import com.thitsaworks.operation_portal.core.scheduler.model.SchedulerConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link SchedulerConfig} entities providing data access operations.
 */
@Repository
public interface SchedulerConfigRepository extends JpaRepository<SchedulerConfig, Long>,
                                                   QuerydslPredicateExecutor<SchedulerConfig> {
    
    /**
     * Find a scheduler configuration by its name.
     *
     * @param name the name of the configuration to find
     * @return an Optional containing the found configuration or empty if not found
     */
    Optional<SchedulerConfig> findByName(String name);
    
    /**
     * Find all active scheduler configurations.
     *
     * @return a list of active scheduler configurations
     */
    List<SchedulerConfig> findByActiveTrue();
    
    /**
     * Find all scheduler configurations with the given active status, with pagination.
     *
     * @param active whether to find active or inactive configurations
     * @param pageable pagination information
     * @return a page of scheduler configurations
     */
    Page<SchedulerConfig> findByActive(boolean active, Pageable pageable);
    
    /**
     * Find all scheduler configurations with the given active status.
     *
     * @param active whether to find active or inactive configurations
     * @return a list of scheduler configurations
     */
    List<SchedulerConfig> findByActive(boolean active);
    
    /**
     * Find all scheduler configurations with the given active status, sorted as specified.
     *
     * @param active whether to find active or inactive configurations
     * @param sort the sort specification
     * @return a list of scheduler configurations
     */
    List<SchedulerConfig> findByActive(boolean active, Sort sort);
    
    /**
     * Find all scheduler configurations, sorted as specified.
     *
     * @param sort the sort specification
     * @return a list of all scheduler configurations
     */
    List<SchedulerConfig> findAll(Sort sort);
}
