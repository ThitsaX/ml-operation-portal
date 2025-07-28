package com.thitsaworks.operation_portal.core.scheduler.query.impl.jpa;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.exception.ResourceNotFoundException;
import com.thitsaworks.operation_portal.core.scheduler.model.SchedulerConfig;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.SchedulerConfigRepository;
import com.thitsaworks.operation_portal.core.scheduler.query.SchedulerConfigQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JPA implementation of the SchedulerConfigQuery interface.
 * Provides methods to retrieve scheduler configurations with optional filtering and sorting.
 */
@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class SchedulerConfigJpaQueryHandler implements SchedulerConfigQuery {

    private final SchedulerConfigRepository schedulerConfigRepository;

    @Override
    public List<SchedulerConfigData> getSchedulerConfigs(Sort sort) {
        return schedulerConfigRepository.findAll(sort).stream()
                .map(SchedulerConfigData::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<SchedulerConfigData> getSchedulerConfigs(boolean active, Sort sort) {
        List<SchedulerConfig> configs = sort == null 
                ? schedulerConfigRepository.findByActive(active)
                : schedulerConfigRepository.findByActive(active, sort);
                
        return configs.stream()
                .map(SchedulerConfigData::new)
                .collect(Collectors.toList());
    }

    @Override
    public SchedulerConfigData get(Long configId) {
        return findById(configId)
                .orElseThrow(() -> new ResourceNotFoundException("SchedulerConfig not found with id: " + configId));
    }

    @Override
    public Optional<SchedulerConfigData> findById(Long configId) {
        return schedulerConfigRepository.findById(configId)
                .map(SchedulerConfigData::new);
    }
}
