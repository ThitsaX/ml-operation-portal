package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.query.SchedulerConfigQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSchedulerConfigById;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetSchedulerConfigByIdHandler
    extends OperationPortalUseCase<GetSchedulerConfigById.Input, GetSchedulerConfigById.Output>
    implements GetSchedulerConfigById {

    private static final Logger LOG = LoggerFactory.getLogger(GetSchedulerConfigByIdHandler.class);

    private final SchedulerConfigQuery schedulerConfigQuery;

    public GetSchedulerConfigByIdHandler(PrincipalCache principalCache,
                                         SchedulerConfigQuery schedulerConfigQuery,
                                         ActionAuthorizationManager actionAuthorizationManager
                                        ) {

        super(principalCache,
              actionAuthorizationManager
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
