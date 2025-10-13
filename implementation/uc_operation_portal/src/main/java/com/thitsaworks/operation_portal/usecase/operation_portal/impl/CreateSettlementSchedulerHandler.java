package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateSchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.settlement.command.AddSettlementSchedulerCommand;
import com.thitsaworks.operation_portal.core.settlement.data.SettlementModelData;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementModelQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateSettlementScheduler;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateSettlementSchedulerHandler
        extends OperationPortalAuditableUseCase<CreateSettlementScheduler.Input, CreateSettlementScheduler.Output>
        implements CreateSettlementScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSettlementSchedulerHandler.class);

    private final SettlementModelQuery settlementModelQuery;

    private final CreateSchedulerConfigCommand createSchedulerConfigCommand;

    private final AddSettlementSchedulerCommand addSettlementSchedulerCommand;

    public CreateSettlementSchedulerHandler(CreateInputAuditCommand createInputAuditCommand,
                                            CreateOutputAuditCommand createOutputAuditCommand,
                                            CreateExceptionAuditCommand createExceptionAuditCommand,
                                            ObjectMapper objectMapper,
                                            PrincipalCache principalCache,
                                            ActionAuthorizationManager actionAuthorizationManager,
                                            SettlementModelQuery settlementModelQuery,
                                            CreateSchedulerConfigCommand createSchedulerConfigCommand,
                                            AddSettlementSchedulerCommand addSettlementSchedulerCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.settlementModelQuery = settlementModelQuery;
        this.createSchedulerConfigCommand = createSchedulerConfigCommand;
        this.addSettlementSchedulerCommand = addSettlementSchedulerCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        SettlementModelData settlementModelData = this.settlementModelQuery.get(input.settlementModelId());

        var schedulerConfigData =
                this.createSchedulerConfigCommand.execute(new CreateSchedulerConfigCommand.Input(input.name(),
                                                                                                 "CloseSettlementWindowsScheduler",
                                                                                                 input.description(),
                                                                                                 input.cronExpression(),
                                                                                                 input.zoneId()));

        var output =
                this.addSettlementSchedulerCommand.execute(new AddSettlementSchedulerCommand.Input(settlementModelData.settlementModelId(),
                                                                                                   schedulerConfigData.schedulerConfigData()
                                                                                                                      .schedulerConfigId()));

        return new Output(output.created(), output.schedulerConfigId());
    }

}
