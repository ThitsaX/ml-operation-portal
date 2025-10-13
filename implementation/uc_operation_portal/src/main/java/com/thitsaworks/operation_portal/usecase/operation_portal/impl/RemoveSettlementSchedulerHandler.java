package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.DeleteSchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.settlement.command.RemoveSettlementSchedulerCommand;
import com.thitsaworks.operation_portal.core.settlement.data.SettlementModelData;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementModelQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveSettlementScheduler;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.SchedulerEngine;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RemoveSettlementSchedulerHandler
        extends OperationPortalAuditableUseCase<RemoveSettlementScheduler.Input, RemoveSettlementScheduler.Output>
        implements RemoveSettlementScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveSettlementSchedulerHandler.class);

    private final SchedulerEngine schedulerEngine;

    private final SettlementModelQuery settlementModelQuery;

    private final DeleteSchedulerConfigCommand deleteSchedulerConfigCommand;

    private final RemoveSettlementSchedulerCommand removeSettlementSchedulerCommand;

    public RemoveSettlementSchedulerHandler(CreateInputAuditCommand createInputAuditCommand,
                                            CreateOutputAuditCommand createOutputAuditCommand,
                                            CreateExceptionAuditCommand createExceptionAuditCommand,
                                            ObjectMapper objectMapper,
                                            PrincipalCache principalCache,
                                            ActionAuthorizationManager actionAuthorizationManager,
                                            SchedulerEngine schedulerEngine,
                                            SettlementModelQuery settlementModelQuery,
                                            DeleteSchedulerConfigCommand deleteSchedulerConfigCommand,
                                            RemoveSettlementSchedulerCommand removeSettlementSchedulerCommand
                                           ) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.schedulerEngine = schedulerEngine;
        this.settlementModelQuery = settlementModelQuery;
        this.deleteSchedulerConfigCommand = deleteSchedulerConfigCommand;
        this.removeSettlementSchedulerCommand = removeSettlementSchedulerCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        SettlementModelData settlementModelData = this.settlementModelQuery.get(input.settlementModelId());

        var schedulerConfigOutput =
                this.deleteSchedulerConfigCommand.execute(input.schedulerConfigId());

        this.schedulerEngine.cancel(schedulerConfigOutput.schedulerConfigData().schedulerConfigId().getId());

        var output =
                this.removeSettlementSchedulerCommand.execute(new RemoveSettlementSchedulerCommand.Input(
                        settlementModelData.settlementModelId(),
                        schedulerConfigOutput.schedulerConfigData().schedulerConfigId()));

        return new Output(output.removed(), output.schedulerConfigId());
    }

}
