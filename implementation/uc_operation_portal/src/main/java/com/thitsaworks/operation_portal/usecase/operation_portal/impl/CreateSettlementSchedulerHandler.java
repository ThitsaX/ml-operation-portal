package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateSchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.query.SchedulerConfigQuery;
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

import java.util.List;

@Service
public class CreateSettlementSchedulerHandler
    extends OperationPortalAuditableUseCase<CreateSettlementScheduler.Input, CreateSettlementScheduler.Output>
    implements CreateSettlementScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSettlementSchedulerHandler.class);

    private final SchedulerEngine schedulerEngine;

    private final SchedulerConfigQuery schedulerConfigQuery;

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
                                            SchedulerConfigQuery schedulerConfigQuery,
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
        this.schedulerConfigQuery = schedulerConfigQuery;
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

        List<SchedulerConfigData> schedulerConfigDataList =
                this.schedulerConfigQuery.getSchedulerConfigs(null);

        List<SchedulerConfigData> settlementSchedulerList =
                schedulerConfigDataList.stream()
                                       .filter(schedulerConfigData -> settlementModelData.schedulerConfigIds()
                                                                                         .contains(schedulerConfigData.schedulerConfigId()))
                                       .toList();

        boolean isOverlap = this.schedulerEngine.isCronOverlap(settlementSchedulerList, input.cronExpression());

        if (isOverlap) {
            throw new SettlementException(SettlementErrors.SETTLEMENT_SCHEDULER_OVERLAP.format(
                    settlementModelData.name()));
        }

        var schedulerConfigOutput =
                this.createSchedulerConfigCommand.execute(new CreateSchedulerConfigCommand.Input(input.name(),
                                                                                                 "SchedulingTester",
                                                                                                 input.description(),
                                                                                                 input.cronExpression(),
                                                                                                 settlementModelData.zoneId()));

        this.addSettlementSchedulerCommand.execute(new AddSettlementSchedulerCommand.Input(settlementModelData.settlementModelId(),
                                                                                           schedulerConfigOutput.schedulerConfigData()
                                                                                                                .schedulerConfigId()));

        this.schedulerEngine.scheduleOrReschedule(schedulerConfigOutput.schedulerConfigData());


        return new Output(true);
    }

}
