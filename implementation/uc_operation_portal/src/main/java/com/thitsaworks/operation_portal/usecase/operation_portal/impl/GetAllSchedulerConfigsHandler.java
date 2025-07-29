package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.query.SchedulerConfigQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetAllSchedulerConfigs;
import com.thitsaworks.operation_portal.usecase.util.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Handler for retrieving all scheduler configurations with optional filtering and sorting.
 */
@Service
public class GetAllSchedulerConfigsHandler
    extends OperationPortalAuditableUseCase<GetAllSchedulerConfigs.Input, GetAllSchedulerConfigs.Output>
    implements GetAllSchedulerConfigs {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllSchedulerConfigsHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(
        UserRoleType.OPERATION,
        UserRoleType.ADMIN
                                                                   );

    private final SchedulerConfigQuery schedulerConfigQuery;

    /**
     * Constructs a new handler with required dependencies.
     */
    public GetAllSchedulerConfigsHandler(CreateInputAuditCommand createInputAuditCommand,
                                         CreateOutputAuditCommand createOutputAuditCommand,
                                         CreateExceptionAuditCommand createExceptionAuditCommand,
                                         ObjectMapper objectMapper,
                                         PrincipalCache principalCache,
                                         SchedulerConfigQuery schedulerConfigQuery,
                                         ActionAuthorizationManager actionAuthorizationManager
                                        ) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.schedulerConfigQuery = schedulerConfigQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        LOG.info("Fetching scheduler configurations with filtering and sorting");

        // Create sort object based on input
        Sort sort = Sort.by(
            input.sortDirection()
                 .orElse(Sort.Direction.ASC),
            input.sortBy()
                 .orElse(DEFAULT_SORT_FIELD)
                           );

        // Fetch filtered and sorted results
        List<SchedulerConfigData> configs = input.active()
                                                 .map(active -> schedulerConfigQuery.getSchedulerConfigs(active, sort))
                                                 .orElseGet(() -> schedulerConfigQuery.getSchedulerConfigs(sort));

        return new Output(configs);
    }

}
