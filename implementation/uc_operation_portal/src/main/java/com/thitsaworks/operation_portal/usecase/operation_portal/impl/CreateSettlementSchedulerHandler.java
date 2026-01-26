package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateSchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.settlement.command.AddSettlementSchedulerCommand;
import com.thitsaworks.operation_portal.core.settlement.data.SettlementModelData;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementErrors;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementException;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementModelQuery;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementSchedulerQuery;
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

    private final ObjectMapper objectMapper;

    private final SchedulerEngine schedulerEngine;

    private final SettlementModelQuery settlementModelQuery;

    private final SettlementSchedulerQuery settlementSchedulerQuery;

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
                                            SettlementSchedulerQuery settlementSchedulerQuery,
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
        this.settlementSchedulerQuery = settlementSchedulerQuery;
        this.createSchedulerConfigCommand = createSchedulerConfigCommand;
        this.addSettlementSchedulerCommand = addSettlementSchedulerCommand;
        this.objectMapper =  objectMapper;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, JsonProcessingException {

        LOG.info("Settlement Model Query Request : settlementModelId {}", input.settlementModelId());

        SettlementModelData settlementModelData = this.settlementModelQuery.get(input.settlementModelId());

        LOG.info("Settlement Model Query Response : {}", this.objectMapper.writeValueAsString(settlementModelData));

        if (!settlementModelData.autoCloseWindow()) {
            throw new SettlementException(SettlementErrors.SETTLEMENT_MODEL_NOT_AUTO_CLOSE_WINDOW.format(
                settlementModelData.name()));
        }

        LOG.info("Get Settlement Schedulers Query Request : settlementModelId : {}", settlementModelData.settlementModelId());

        List<SchedulerConfigData> schedulerConfigDataList = this.settlementSchedulerQuery.getSettlementSchedulers(
            settlementModelData.settlementModelId());

        LOG.info("Get Settlement Schedulers Query Response : {}", this.objectMapper.writeValueAsString(schedulerConfigDataList));

        boolean isOverlap = this.schedulerEngine.isCronOverlap(schedulerConfigDataList, input.cronExpression());

        if (isOverlap) {
            throw new SettlementException(SettlementErrors.SETTLEMENT_SCHEDULER_OVERLAP.format(
                settlementModelData.name()));
        }

        var schedulerConfigOutput =
            this.createSchedulerConfigCommand.execute(new CreateSchedulerConfigCommand.Input(input.name(),
                                                                                             "CloseSettlementWindowsScheduler",
                                                                                             input.description(),
                                                                                             input.cronExpression(),
                                                                                             settlementModelData.zoneId()));

        this.addSettlementSchedulerCommand.execute(new AddSettlementSchedulerCommand.Input(settlementModelData.settlementModelId(),
                                                                                           schedulerConfigOutput.schedulerConfigData()
                                                                                                                .schedulerConfigId()));

        LOG.info("Scheduler Engine Schedule or Reschedule Request : {}", this.objectMapper.writeValueAsString(schedulerConfigOutput.schedulerConfigData()));

        this.schedulerEngine.scheduleOrReschedule(schedulerConfigOutput.schedulerConfigData());

        return new Output(schedulerConfigOutput.schedulerConfigData()
                                               .schedulerConfigId(),
                          true);
    }

}
