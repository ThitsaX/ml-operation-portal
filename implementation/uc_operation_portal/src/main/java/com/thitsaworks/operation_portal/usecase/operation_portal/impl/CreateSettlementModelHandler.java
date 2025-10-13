package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateSchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.settlement.command.CreateSettlementModelCommand;
import com.thitsaworks.operation_portal.core.settlement.data.SettlementModelData;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementErrors;
import com.thitsaworks.operation_portal.core.settlement.exception.SettlementException;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementModelQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateSettlementModel;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.SchedulerEngine;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CreateSettlementModelHandler
        extends OperationPortalAuditableUseCase<CreateSettlementModel.Input, CreateSettlementModel.Output>
        implements CreateSettlementModel {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSettlementModelHandler.class);

    private final SettlementModelQuery settlementModelQuery;

    private final CreateSettlementModelCommand createSettlementModelCommand;

    private final CreateSchedulerConfigCommand createSchedulerConfigCommand;

    private final SchedulerEngine schedulerEngine;

    private final ObjectMapper objectMapper;

    public CreateSettlementModelHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ObjectMapper objectMapper,
                                        PrincipalCache principalCache,
                                        ActionAuthorizationManager actionAuthorizationManager,
                                        SettlementModelQuery settlementModelQuery,
                                        CreateSettlementModelCommand createSettlementModelCommand,
                                        CreateSchedulerConfigCommand createSchedulerConfigCommand,
                                        SchedulerEngine schedulerEngine) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.settlementModelQuery = settlementModelQuery;
        this.createSettlementModelCommand = createSettlementModelCommand;
        this.createSchedulerConfigCommand = createSchedulerConfigCommand;
        this.schedulerEngine = schedulerEngine;
        this.objectMapper = objectMapper;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        Optional<SettlementModelData> optionalSettlementModelData =
                this.settlementModelQuery.get(input.settlementModelName());

        if (optionalSettlementModelData.isPresent()) {

            throw new SettlementException(SettlementErrors.SETTLEMENT_MODEL_ALREADY_REGISTERED.format(input.settlementModelName()));

        }

        List<SchedulerConfigId> schedulerConfigIdList = new ArrayList<>();

        if (input.autoCloseWindow() && !input.schedulerConfigInfoList().isEmpty()) {

            for (Input.SchedulerConfigInfo schedulerConfigInfo : input.schedulerConfigInfoList()) {
                CreateSchedulerConfigCommand.Output schedulerOutput =
                        this.createSchedulerConfigCommand.execute(new CreateSchedulerConfigCommand.Input(
                                schedulerConfigInfo.name(),
                                "CloseSettlementWindowsScheduler",
                                schedulerConfigInfo.description(),
                                schedulerConfigInfo.cronExpression(),
                                schedulerConfigInfo.zoneId()));

                this.schedulerEngine.scheduleOrReschedule(schedulerOutput.schedulerConfigData());

                schedulerConfigIdList.add(schedulerOutput.schedulerConfigData().schedulerConfigId());
            }

        }

        var output = this.createSettlementModelCommand.execute(new CreateSettlementModelCommand.Input(

                input.settlementModelName(),
                input.modelType(),
                input.currencyID(),
                true,
                input.autoCloseWindow(),
                input.requireLiquidityCheck(),
                input.autoPositionReset(),
                input.adjustPosition(),
                schedulerConfigIdList));

        return new Output(output.created(), output.settlementModelId());

    }

}
