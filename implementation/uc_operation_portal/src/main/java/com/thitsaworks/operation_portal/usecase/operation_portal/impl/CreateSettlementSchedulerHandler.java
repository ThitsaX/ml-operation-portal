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
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementErrors;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementException;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementModelQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateSettlementScheduler;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.SchedulerEngine;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateSettlementSchedulerHandler
    extends OperationPortalAuditableUseCase<CreateSettlementScheduler.Input, CreateSettlementScheduler.Output>
    implements CreateSettlementScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSettlementSchedulerHandler.class);

    private final SchedulerEngine schedulerEngine;

    private final SettlementModelQuery settlementModelQuery;

    private final CreateSchedulerConfigCommand createSchedulerConfigCommand;

    private final AddSettlementSchedulerCommand addSettlementSchedulerCommand;

    public CreateSettlementSchedulerHandler(CreateInputAuditCommand createInputAuditCommand,
                                            CreateOutputAuditCommand createOutputAuditCommand,
                                            CreateExceptionAuditCommand createExceptionAuditCommand,
                                            ObjectMapper objectMapper,
                                            PrincipalCache principalCache,
                                            ActionAuthorizationManager actionAuthorizationManager,
                                            SchedulerEngine schedulerEngine,
                                            SettlementModelQuery settlementModelQuery,
                                            CreateSchedulerConfigCommand createSchedulerConfigCommand,
                                            AddSettlementSchedulerCommand addSettlementSchedulerCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.schedulerEngine = schedulerEngine;
        this.settlementModelQuery = settlementModelQuery;
        this.createSchedulerConfigCommand = createSchedulerConfigCommand;
        this.addSettlementSchedulerCommand = addSettlementSchedulerCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        SettlementModelData settlementModelData = this.settlementModelQuery.get(input.settlementModelId());

        if (!settlementModelData.autoCloseWindow()) {
            throw new SettlementException(SettlementErrors.SETTLEMENT_MODEL_NOT_AUTO_CLOSE_WINDOW.format(
                settlementModelData.name()));
        }

        for (var config : input.schedulerConfigInfoList()) {
            var schedulerConfigOutput =
                this.createSchedulerConfigCommand.execute(new CreateSchedulerConfigCommand.Input(config.name(),
                                                                                                 "CloseSettlementWindowsScheduler",
                                                                                                 config.description(),
                                                                                                 config.cronExpression(),
                                                                                                 config.zoneId()));

            this.addSettlementSchedulerCommand.execute(new AddSettlementSchedulerCommand.Input(settlementModelData.settlementModelId(),
                                                                                               schedulerConfigOutput.schedulerConfigData()
                                                                                                                    .schedulerConfigId()));

            this.schedulerEngine.scheduleOrReschedule(schedulerConfigOutput.schedulerConfigData());

        }

        return new Output(true);
    }

}
