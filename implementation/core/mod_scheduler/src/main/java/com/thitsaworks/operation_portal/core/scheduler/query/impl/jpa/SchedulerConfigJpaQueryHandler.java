package com.thitsaworks.operation_portal.core.scheduler.query.impl.jpa;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.exception.SchedulerErrors;
import com.thitsaworks.operation_portal.core.scheduler.exception.SchedulerException;
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
    public SchedulerConfigData get(SchedulerConfigId schedulerConfigId) throws DomainException {

        return findById(schedulerConfigId)
                .orElseThrow(() -> new SchedulerException(SchedulerErrors.SCHEDULER_CONFIG_NOT_FOUND.format(
                        schedulerConfigId)));
    }

    @Override
    public Optional<SchedulerConfigData> findById(SchedulerConfigId schedulerConfigId) {

        return schedulerConfigRepository.findById(schedulerConfigId)
                .map(SchedulerConfigData::new);
    }
}
