package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.query.SchedulerConfigQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSchedulerConfigList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handler for retrieving all scheduler configurations with optional filtering and sorting.
 */
@Service
public class GetSchedulerConfigListHandler
    extends OperationPortalAuditableUseCase<GetSchedulerConfigList.Input, GetSchedulerConfigList.Output>
    implements GetSchedulerConfigList {

    private static final Logger LOG = LoggerFactory.getLogger(GetSchedulerConfigListHandler.class);

    private final SchedulerConfigQuery schedulerConfigQuery;

    /**
     * Constructs a new handler with required dependencies.
     */
    public GetSchedulerConfigListHandler(CreateInputAuditCommand createInputAuditCommand,
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
            input.sortDirection(),
            input.sortBy()
        );
        
        // Fetch filtered and sorted results
        List<SchedulerConfigData> configs = schedulerConfigQuery.getSchedulerConfigs(input.active(), sort);
        
        return new Output(configs);
    }

}
