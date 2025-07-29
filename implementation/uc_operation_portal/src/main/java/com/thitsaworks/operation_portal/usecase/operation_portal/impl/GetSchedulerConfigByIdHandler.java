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
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSchedulerConfigById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GetSchedulerConfigByIdHandler
    extends OperationPortalAuditableUseCase<GetSchedulerConfigById.Input, GetSchedulerConfigById.Output>
    implements GetSchedulerConfigById {

    private static final Logger LOG = LoggerFactory.getLogger(GetSchedulerConfigByIdHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(
        UserRoleType.OPERATION,
        UserRoleType.ADMIN
    );

    private final SchedulerConfigQuery schedulerConfigQuery;

    public GetSchedulerConfigByIdHandler(
        CreateInputAuditCommand createInputAuditCommand,
        CreateOutputAuditCommand createOutputAuditCommand,
        CreateExceptionAuditCommand createExceptionAuditCommand,
        ObjectMapper objectMapper,
        PrincipalCache principalCache,
        SchedulerConfigQuery schedulerConfigQuery
    ) {
        super(
            createInputAuditCommand,
            createOutputAuditCommand,
            createExceptionAuditCommand,
            PERMITTED_ROLES,
            objectMapper,
            principalCache
        );
        this.schedulerConfigQuery = schedulerConfigQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {
        LOG.info("Fetching scheduler configuration with ID: {}", input.configId());
        SchedulerConfigData config = schedulerConfigQuery.get(input.configId());
        return new Output(config);
    }
}
